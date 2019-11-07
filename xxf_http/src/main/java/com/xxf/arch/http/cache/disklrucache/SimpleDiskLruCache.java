package com.xxf.arch.http.cache.disklrucache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class SimpleDiskLruCache {

    private static final int VALUE_IDX = 0;
    private static final int METADATA_IDX = 1;

    private DiskLruCache diskLruCache;
    private int mAppVersion;

    private SimpleDiskLruCache(File dir, int appVersion, long maxSize) throws IOException {
        mAppVersion = appVersion;
        diskLruCache = DiskLruCache.open(dir, appVersion, 2, maxSize);
    }

    public static synchronized SimpleDiskLruCache open(File dir, int appVersion, long maxSize)
            throws IOException {
        return new SimpleDiskLruCache(dir, appVersion, maxSize);
    }

    /**
     * User should be sure there are no outstanding operations.
     *
     * @throws IOException
     */
    public void clear() throws IOException {
        File dir = diskLruCache.getDirectory();
        long maxSize = diskLruCache.getMaxSize();
        diskLruCache.delete();
        diskLruCache = DiskLruCache.open(dir, mAppVersion, 2, maxSize);
    }

    public DiskLruCache getCache() {
        return diskLruCache;
    }

    public InputStreamEntry getInputStream(String key) throws IOException {
        DiskLruCache.Snapshot snapshot = diskLruCache.get(toInternalKey(key));
        if (snapshot == null) return null;
        return new InputStreamEntry(snapshot, readMetadata(snapshot));
    }

    public BitmapEntry getBitmap(String key) throws IOException {
        DiskLruCache.Snapshot snapshot = diskLruCache.get(toInternalKey(key));
        if (snapshot == null) return null;

        try {
            Bitmap bitmap = BitmapFactory.decodeStream(snapshot.getInputStream(VALUE_IDX));
            return new BitmapEntry(bitmap, readMetadata(snapshot));
        } finally {
            snapshot.close();
        }
    }

    public StringEntry getString(String key) throws IOException {
        DiskLruCache.Snapshot snapshot = diskLruCache.get(toInternalKey(key));
        if (snapshot == null) return null;

        try {
            return new StringEntry(snapshot.getString(VALUE_IDX), readMetadata(snapshot));
        } finally {
            snapshot.close();
        }
    }

    public boolean contains(String key) throws IOException {
        DiskLruCache.Snapshot snapshot = diskLruCache.get(toInternalKey(key));
        if (snapshot == null) return false;

        snapshot.close();
        return true;
    }

    public OutputStream openStream(String key) throws IOException {
        return openStream(key, new HashMap<String, Serializable>());
    }

    public OutputStream openStream(String key, Map<String, ? extends Serializable> metadata)
            throws IOException {
        DiskLruCache.Editor editor = diskLruCache.edit(toInternalKey(key));
        try {
            writeMetadata(metadata, editor);
            BufferedOutputStream bos = new BufferedOutputStream(editor.newOutputStream(VALUE_IDX));
            return new CacheOutputStream(bos, editor);
        } catch (IOException e) {
            editor.abort();
            throw e;
        }
    }

    public void put(String key, InputStream is) throws IOException {
        put(key, is, new HashMap<String, Serializable>());
    }

    public void put(String key, InputStream is, Map<String, Serializable> annotations)
            throws IOException {
        OutputStream os = null;
        try {
            os = openStream(key, annotations);
            copy(is, os);
        } finally {
            if (os != null) os.close();
        }
    }

    public void put(String key, String value) throws IOException {
        put(key, value, new HashMap<String, Serializable>());
    }

    public void put(String key, String value, Map<String, ? extends Serializable> annotations)
            throws IOException {
        OutputStream cos = null;
        try {
            cos = openStream(key, annotations);
            cos.write(value.getBytes());
        } finally {
            if (cos != null) cos.close();
        }

    }

    private void writeMetadata(Map<String, ? extends Serializable> metadata,
                               DiskLruCache.Editor editor) throws IOException {
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new BufferedOutputStream(
                    editor.newOutputStream(METADATA_IDX)));
            oos.writeObject(metadata);
        } finally {
            closeQuietly(oos);
        }
    }

    private Map<String, Serializable> readMetadata(DiskLruCache.Snapshot snapshot)
            throws IOException {
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new BufferedInputStream(
                    snapshot.getInputStream(METADATA_IDX)));
            @SuppressWarnings("unchecked")
            Map<String, Serializable> annotations = (Map<String, Serializable>) ois.readObject();
            return annotations;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            closeQuietly(ois);
        }
    }

    private String toInternalKey(String key) {
        return md5(key);
    }

    private String md5(String s) {
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(s.getBytes("UTF-8"));
            byte[] digest = m.digest();
            BigInteger bigInt = new BigInteger(1, digest);
            return bigInt.toString(16);
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError();
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError();
        }
    }

    private class CacheOutputStream extends FilterOutputStream {

        private final DiskLruCache.Editor editor;
        private boolean failed = false;

        private CacheOutputStream(OutputStream os, DiskLruCache.Editor editor) {
            super(os);
            this.editor = editor;
        }

        @Override
        public void close() throws IOException {
            IOException closeException = null;
            try {
                super.close();
            } catch (IOException e) {
                closeException = e;
            }

            if (failed) {
                editor.abort();
            } else {
                editor.commit();
            }

            if (closeException != null) throw closeException;
        }

        @Override
        public void flush() throws IOException {
            try {
                super.flush();
            } catch (IOException e) {
                failed = true;
                throw e;
            }
        }

        @Override
        public void write(int oneByte) throws IOException {
            try {
                super.write(oneByte);
            } catch (IOException e) {
                failed = true;
                throw e;
            }
        }

        @Override
        public void write(byte[] buffer) throws IOException {
            try {
                super.write(buffer);
            } catch (IOException e) {
                failed = true;
                throw e;
            }
        }

        @Override
        public void write(byte[] buffer, int offset, int length) throws IOException {
            try {
                super.write(buffer, offset, length);
            } catch (IOException e) {
                failed = true;
                throw e;
            }
        }
    }

    public static class InputStreamEntry {
        private final DiskLruCache.Snapshot snapshot;
        private final Map<String, Serializable> metadata;

        public InputStreamEntry(DiskLruCache.Snapshot snapshot, Map<String, Serializable> metadata) {
            this.metadata = metadata;
            this.snapshot = snapshot;
        }

        public InputStream getInputStream() {
            return snapshot.getInputStream(VALUE_IDX);
        }

        public Map<String, Serializable> getMetadata() {
            return metadata;
        }

        public void close() {
            snapshot.close();

        }

    }

    public static class BitmapEntry {
        private final Bitmap bitmap;
        private final Map<String, Serializable> metadata;

        public BitmapEntry(Bitmap bitmap, Map<String, Serializable> metadata) {
            this.bitmap = bitmap;
            this.metadata = metadata;
        }

        public Bitmap getBitmap() {
            return bitmap;
        }

        public Map<String, Serializable> getMetadata() {
            return metadata;
        }
    }

    public static class StringEntry {
        private final String string;
        private final Map<String, Serializable> metadata;

        public StringEntry(String string, Map<String, Serializable> metadata) {
            this.string = string;
            this.metadata = metadata;
        }

        public String getString() {
            return string;
        }

        public Map<String, Serializable> getMetadata() {
            return metadata;
        }
    }

    private static int copy(InputStream input, OutputStream output) throws IOException {
        long count = copyLarge(input, output);
        return count > 2147483647L ? -1 : (int) count;
    }

    public static long copy(InputStream input, OutputStream output, int bufferSize) throws IOException {
        return copyLarge(input, output, new byte[bufferSize]);
    }

    public static long copyLarge(InputStream input, OutputStream output) throws IOException {
        return copy(input, output, 4096);
    }

    public static long copyLarge(InputStream input, OutputStream output, byte[] buffer) throws IOException {
        long count;
        int n;
        for (count = 0L; -1 != (n = input.read(buffer)); count += (long) n) {
            output.write(buffer, 0, n);
        }

        return count;
    }

    public static void closeQuietly(OutputStream output) {
        closeQuietly((Closeable) output);
    }

    public static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException var2) {
        }

    }
}