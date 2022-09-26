package com.xxf.arch.utils;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.FileUtils;
import android.os.ParcelFileDescriptor;
import android.os.storage.StorageManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;

import androidx.annotation.AnyRes;
import androidx.annotation.CheckResult;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.WorkerThread;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Method;

import static android.content.ContentResolver.SCHEME_CONTENT;
import static android.content.Context.STORAGE_SERVICE;

import com.xxf.hash.Murmur3A;
import com.xxf.hash.PrimitiveDataChecksum;

/**
 * Description
 * Company Beijing icourt
 *
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2017/4/8
 * version 1.0.0
 */
public class UriUtils {

    /**
     * Uri to file.
     *
     * @param uri The uri.
     * @return file
     */
    public static File uri2File(final Context context, final Uri uri) {
        if (uri == null) return null;
        File file = uri2FileReal(context, uri);
        if (file != null) return file;
        return copyUri2Cache(context, uri);
    }

    /**
     * Uri to file.
     *
     * @param uri The uri.
     * @return file
     */
    private static File uri2FileReal(final Context context, final Uri uri) {
        Log.d("UriUtils", uri.toString());
        String authority = uri.getAuthority();
        String scheme = uri.getScheme();
        String path = uri.getPath();

        /**
         * 先执行拷贝
         */
        if (scheme.equals(ContentResolver.SCHEME_CONTENT)) {
            File file = copyUri2Cache(context, uri);
            if (file.exists() && file.length() > 0) {
                return file;
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && path != null) {
            String[] externals = new String[]{"/external/", "/external_path/"};
            File file = null;
            for (String external : externals) {
                if (path.startsWith(external)) {
                    file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                            + path.replace(external, "/"));
                    if (file.exists()) {
                        Log.d("UriUtils", uri.toString() + " -> " + external);
                        return file;
                    }
                }
            }
            file = null;
            if (path.startsWith("/files_path/")) {
                file = new File(context.getFilesDir().getAbsolutePath()
                        + path.replace("/files_path/", "/"));
            } else if (path.startsWith("/cache_path/")) {
                file = new File(context.getCacheDir().getAbsolutePath()
                        + path.replace("/cache_path/", "/"));
            } else if (path.startsWith("/external_files_path/")) {
                file = new File(context.getExternalFilesDir(null).getAbsolutePath()
                        + path.replace("/external_files_path/", "/"));
            } else if (path.startsWith("/external_cache_path/")) {
                file = new File(context.getExternalCacheDir().getAbsolutePath()
                        + path.replace("/external_cache_path/", "/"));
            }
            if (file != null && file.exists()) {
                Log.d("UriUtils", uri.toString() + " -> " + path);
                return file;
            }
        }
        if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            if (path != null) return new File(path);
            Log.d("UriUtils", uri.toString() + " parse failed. -> 0");
            return null;
        }// end 0
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && DocumentsContract.isDocumentUri(context, uri)) {
            if ("com.android.externalstorage.documents".equals(authority)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return new File(Environment.getExternalStorageDirectory() + "/" + split[1]);
                } else {
                    // Below logic is how External Storage provider build URI for documents
                    // http://stackoverflow.com/questions/28605278/android-5-sd-card-label
                    StorageManager mStorageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
                    try {
                        Class<?> storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
                        Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList");
                        Method getUuid = storageVolumeClazz.getMethod("getUuid");
                        Method getState = storageVolumeClazz.getMethod("getState");
                        Method getPath = storageVolumeClazz.getMethod("getPath");
                        Method isPrimary = storageVolumeClazz.getMethod("isPrimary");
                        Method isEmulated = storageVolumeClazz.getMethod("isEmulated");

                        Object result = getVolumeList.invoke(mStorageManager);

                        final int length = Array.getLength(result);
                        for (int i = 0; i < length; i++) {
                            Object storageVolumeElement = Array.get(result, i);
                            //String uuid = (String) getUuid.invoke(storageVolumeElement);

                            final boolean mounted = Environment.MEDIA_MOUNTED.equals(getState.invoke(storageVolumeElement))
                                    || Environment.MEDIA_MOUNTED_READ_ONLY.equals(getState.invoke(storageVolumeElement));

                            //if the media is not mounted, we need not get the volume details
                            if (!mounted) continue;

                            //Primary storage is already handled.
                            if ((Boolean) isPrimary.invoke(storageVolumeElement)
                                    && (Boolean) isEmulated.invoke(storageVolumeElement)) {
                                continue;
                            }

                            String uuid = (String) getUuid.invoke(storageVolumeElement);

                            if (uuid != null && uuid.equals(type)) {
                                return new File(getPath.invoke(storageVolumeElement) + "/" + split[1]);
                            }
                        }
                    } catch (Exception ex) {
                        Log.d("UriUtils", uri.toString() + " parse failed. " + ex.toString() + " -> 1_0");
                    }
                }
                Log.d("UriUtils", uri.toString() + " parse failed. -> 1_0");
                return null;
            }// end 1_0
            else if ("com.android.providers.downloads.documents".equals(authority)) {
                String id = DocumentsContract.getDocumentId(uri);
                if (TextUtils.isEmpty(id)) {
                    Log.d("UriUtils", uri.toString() + " parse failed(id is null). -> 1_1");
                    return null;
                }
                if (id.startsWith("raw:")) {
                    return new File(id.substring(4));
                } else if (id.startsWith("msf:")) {
                    id = id.split(":")[1];
                }

                long availableId = 0;
                try {
                    availableId = Long.parseLong(id);
                } catch (Exception e) {
                    return null;
                }

                String[] contentUriPrefixesToTry = new String[]{
                        "content://downloads/public_downloads",
                        "content://downloads/all_downloads",
                        "content://downloads/my_downloads"
                };

                for (String contentUriPrefix : contentUriPrefixesToTry) {
                    Uri contentUri = ContentUris.withAppendedId(Uri.parse(contentUriPrefix), availableId);
                    try {
                        File file = getFileFromUri(context, contentUri, "1_1");
                        if (file != null) {
                            return file;
                        }
                    } catch (Exception ignore) {
                    }
                }

                Log.d("UriUtils", uri.toString() + " parse failed. -> 1_1");
                return null;
            }// end 1_1
            else if ("com.android.providers.media.documents".equals(authority)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                } else {
                    Log.d("UriUtils", uri.toString() + " parse failed. -> 1_2");
                    return null;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getFileFromUri(context, contentUri, selection, selectionArgs, "1_2");
            }// end 1_2
            else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
                return getFileFromUri(context, uri, "1_3");
            }// end 1_3
            else {
                Log.d("UriUtils", uri.toString() + " parse failed. -> 1_4");
                return null;
            }// end 1_4
        }// end 1
        else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            return getFileFromUri(context, uri, "2");
        }// end 2
        else {
            Log.d("UriUtils", uri.toString() + " parse failed. -> 3");
            return null;
        }// end 3
    }

    private static File getFileFromUri(final Context context, final Uri uri, final String code) {
        return getFileFromUri(context, uri, null, null, code);
    }

    private static File getFileFromUri(final Context context, final Uri uri,
                                       final String selection,
                                       final String[] selectionArgs,
                                       final String code) {
        if ("com.google.android.apps.photos.content".equals(uri.getAuthority())) {
            if (!TextUtils.isEmpty(uri.getLastPathSegment())) {
                return new File(uri.getLastPathSegment());
            }
        } else if ("com.tencent.mtt.fileprovider".equals(uri.getAuthority())) {
            String path = uri.getPath();
            if (!TextUtils.isEmpty(path)) {
                File fileDir = Environment.getExternalStorageDirectory();
                return new File(fileDir, path.substring("/QQBrowser".length(), path.length()));
            }
        } else if ("com.huawei.hidisk.fileprovider".equals(uri.getAuthority())) {
            String path = uri.getPath();
            if (!TextUtils.isEmpty(path)) {
                return new File(path.replace("/root", ""));
            }
        }

        final Cursor cursor = context.getContentResolver().query(
                uri, new String[]{"_data"}, selection, selectionArgs, null);
        if (cursor == null) {
            Log.d("UriUtils", uri.toString() + " parse failed(cursor is null). -> " + code);
            return null;
        }
        try {
            if (cursor.moveToFirst()) {
                final int columnIndex = cursor.getColumnIndex("_data");
                if (columnIndex > -1) {
                    return new File(cursor.getString(columnIndex));
                } else {
                    Log.d("UriUtils", uri.toString() + " parse failed(columnIndex: " + columnIndex + " is wrong). -> " + code);
                    return null;
                }
            } else {
                Log.d("UriUtils", uri.toString() + " parse failed(moveToFirst return false). -> " + code);
                return null;
            }
        } catch (Exception e) {
            Log.d("UriUtils", uri.toString() + " parse failed. -> " + code);
            return null;
        } finally {
            cursor.close();
        }
    }

    /**
     * 拷贝到沙盒里面
     *
     * @param context
     * @param uri
     * @return
     */
    @SuppressLint("Range")
    private static File copyUri2Cache(final Context context, Uri uri) {
        File file = null;
        if (uri == null) return null;
        //android10以上转换
        if (uri.getScheme().equals(ContentResolver.SCHEME_FILE)) {
            file = new File(uri.getPath());
        } else if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            //把文件复制到沙盒目录
            ContentResolver contentResolver = context.getContentResolver();
            String displayName = null;
            long size = 0;

//            注释掉的方法可以获取到原文件的文件名，但是比较耗时
            Cursor cursor = contentResolver.query(uri, null, null, null, null);
            if (cursor.moveToFirst()) {
                displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                size = cursor.getLong(cursor.getColumnIndex(OpenableColumns.SIZE));
            }
            if (TextUtils.isEmpty(displayName)) {
                displayName = System.currentTimeMillis() + Math.round((Math.random() + 1) * 1000)
                        + "." + MimeTypeMap.getSingleton().getExtensionFromMimeType(contentResolver.getType(uri));
            }

            //防止文件大小发生变化 doc 文件内容可变
            String hashStr = String.valueOf(generateId(uri.toString() + size));

            /**
             * 生成唯一路径
             * 要完整保留 文件名 不变化
             */
            File dstDir = new File(getUriCopyTempDir(context), hashStr);
            if (!dstDir.exists()) {
                dstDir.mkdirs();
            }
            file = new File(dstDir, displayName);
            //避免重复拷贝
            if (size <= 0 || !file.exists() || file.length() <= 0) {
                try {
                    InputStream is = contentResolver.openInputStream(uri);
                    com.xxf.utils.FileUtils.writeFileFromIS(file, is, false);
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }


    /**
     * 获取文件路径
     */
    @WorkerThread
    @CheckResult
    @Nullable
    public static String getPath(final Context context, final Uri uri) {
        File file = uri2File(context, uri);
        if (file.exists()) {
            return file.getAbsolutePath();
        } else {
            return null;
        }
    }

    /**
     * 获取uri copy的临时文件夹
     * 适配android 10 是将uri 的二进制拷贝进来
     *
     * @return
     */
    public static File getUriCopyTempDir(Context context) {
        File dir = new File(context.getCacheDir(), "xxf_uri_copy_temp_dir");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * 清理 临时文件 默认阀门2G
     *
     * @param context
     * @return
     */
    @WorkerThread
    public static boolean clearUriCopyTempDir(Context context) {
        return clearUriCopyTempDir(context, 2 * 1024 * 1024 * 1024);
    }

    /**
     * @param context
     * @param thresholdSize 阀值 超过多少删除
     * @return
     */
    @WorkerThread
    public static boolean clearUriCopyTempDir(Context context, long thresholdSize) {
        File uriCopyTempDir = getUriCopyTempDir(context);
        long fileLength = com.xxf.utils.FileUtils.getFileLength(uriCopyTempDir.getAbsolutePath());
        if (fileLength >= thresholdSize) {
            return com.xxf.utils.FileUtils.delete(uriCopyTempDir);
        }
        return false;
    }

    /**
     * 生成id
     *
     * @param
     * @return
     */
    private static long generateId(String data) {
        PrimitiveDataChecksum primitiveDataChecksum = new PrimitiveDataChecksum(new Murmur3A());
        primitiveDataChecksum.updateUtf8(data);
        return primitiveDataChecksum.getValue();
    }


    /**
     * 是否是文件描述符
     *
     * @param filePath
     * @return
     */
    public static boolean isFileDescriptor(String filePath) {
        try {
            Uri fileProviderUri = Uri.parse(filePath);
            return TextUtils.equals(fileProviderUri.getScheme(), SCHEME_CONTENT);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }

    @Nullable
    @CheckResult
    public static ParcelFileDescriptor getFileDescriptorSafe(Context context, String filePath) {
        try {
            Uri fileProviderUri = Uri.parse(filePath);
            return context.getContentResolver().openFileDescriptor(fileProviderUri, "r");
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取android 7.0文件共享目录下文件描述
     * 用完之后 请close
     *
     * @param context
     * @param uri
     */
    public static ParcelFileDescriptor getFileDescriptor(Context context, Uri uri)
            throws FileNotFoundException {
        return context.getContentResolver().openFileDescriptor(uri, "r");
    }

    /**
     * 文件描述转byte
     *
     * @param pfd
     * @return
     */
    public static final byte[] fileDescriptor2Byte(ParcelFileDescriptor pfd) {
        byte[] bytes = null;
        try {
            bytes = inputStream2Byte(new FileInputStream(pfd.getFileDescriptor()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }

    /**
     * 流转byte
     *
     * @param is
     * @return
     */
    static final byte[] inputStream2Byte(InputStream is) {
        byte[] buffer = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = is.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            is.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return buffer;
    }

    /**
     * 获取外部SD卡的路径
     *
     * @param context
     * @return 获取成功返回外部SD卡的路径，否则返回null
     */
    public static String getExternalSdCardPath(Context context) {
        if (context == null) {
            return null;
        }
        try {
            StorageManager sm = (StorageManager) context.getSystemService(STORAGE_SERVICE);
            Method getVolumePathsMethod = StorageManager.class.getMethod("getVolumePaths", new Class[0]);
            String[] paths = (String[]) getVolumePathsMethod.invoke(sm, new Object[0]);
            return (paths == null || paths.length <= 1) ? null : paths[1];
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * 转换id->uri
     *
     * @param resId
     * @param context
     * @return
     */
    public static final Uri getResourceUri(@IdRes int resId, @NonNull Context context) {
        if (context == null) {
            return Uri.parse("");
        }
        return Uri.parse("android.resource://" + context.getPackageName() + "/" + resId);
    }

    /**
     * 转换id->uri
     *
     * @param resId
     * @param context
     * @return
     */
    public static final String getResourceUriString(@AnyRes int resId, @NonNull Context context) {
        return getResourceUri(resId, context).toString();
    }
}
