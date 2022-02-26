package com.xxf.arch.utils;

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
     * 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
     * 注意先 获取文件读写权限
     */
    @WorkerThread
    public static String getPath(final Context context, final Uri uri) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                return uriToFileApiQ(uri, context).getAbsolutePath();
            }
            // DocumentProvider

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                    && DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    } else {// handle non-primary volumes
                        String sdCardPath = getExternalSdCardPath(context);
                        if (sdCardPath != null) {
                            return sdCardPath + "/" + split[1];
                        }
                    }
                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {

                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                    return getDataColumn(context, contentUri, null, null);
                }
                // MediaProvider
                else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{split[1]};

                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
            }
            // MediaStore (and general)
            else if ("content".equalsIgnoreCase(uri.getScheme())) {
                try {
                    return getDataColumn(context, uri, null, null);
                } catch (Exception e) {
                    return uri.toString();
                }
            }
            // File
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 适配android 10
     *
     * @param uri
     * @param context
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @WorkerThread
    public static File uriToFileApiQ(Uri uri, Context context) {
        File file = null;
        if (uri == null) return file;
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
                    FileOutputStream fos = new FileOutputStream(file);
                    FileUtils.copy(is, fos);
                    fos.close();
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
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
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = MediaStore.MediaColumns.DATA;
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
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
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
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
