package com.xxf.arch.http;

import android.content.Context;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;

import com.xxf.utils.UriUtils;

import java.io.File;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 过时了 请直接使用
 * Uri.toRequestBody
 * File.toRequestBody
 * FileDescriptor.toRequestBody
 * ...
 * File.toPart
 * Uri.toPart
 * FileDescriptor.toPart
 * .....
 *
 * <p>
 * Description
 * <p>
 *
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2017/4/10
 * version 1.0.0
 */
@Deprecated
public class RequestUtils {

    /**
     * createFilePart
     * 构建文本请求体
     *
     * @param text
     * @return
     */
    @Deprecated
    public static RequestBody createTextBody(String text) {
        return RequestBody
                .create(MediaType.parse("text/plain"),
                        TextUtils.isEmpty(text) ? "" : text);
    }

    /**
     * 构建图片请求体
     * 请用 {@link #createFilePart(Context, String, String)}
     *
     * @param file
     * @return
     */
    @Deprecated
    public static RequestBody createImgBody(File file) {
        if (file != null && file.exists()) {
            return RequestBody.create(MediaType.parse("image/*"), file);
        }
        return null;
    }

    /**
     * 过时了 请直接使用RequestBody.toXXX方
     * 构建表单请求体
     * 兼容 文件描述符
     *
     * @param path 文件路径
     * @return
     */
    @Deprecated
    public static RequestBody createFileBody(Context context, String path) {
        try {
            ParcelFileDescriptor pfd = UriUtils.getFileDescriptorSafe(context, path);
            if (pfd != null) {
                byte[] bytes = UriUtils.fileDescriptor2Byte(pfd);
                return RequestUtils.createFileBody(bytes);
            } else {
                File file = new File(path);
                if (file != null && file.exists()) {
                    return RequestBody.create(MediaType.parse("multipart/form-data"), file);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 过时了 请直接使用RequestBody.toXXX方
     * 构建表单请求体
     * 支持文件描述符
     *
     * @param context
     * @param formKey
     * @param path
     * @return
     */
    @Deprecated
    public static MultipartBody.Part createFilePart(Context context, String formKey, String path) {
        try {
            if (UriUtils.isFileDescriptor(path)) {
                Uri uri = Uri.parse(path);
                return MultipartBody.Part.createFormData(formKey, TextUtils.isEmpty(uri.getLastPathSegment()) ? UUID.randomUUID().toString() : uri.getLastPathSegment(), RequestUtils.createFileBody(context, path));
            } else {
                File file = new File(path);
                if (file != null && file.exists()) {
                    return MultipartBody.Part.createFormData(formKey, file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 构建表单请求体
     *
     * @return
     */
    @Deprecated
    public static RequestBody createFileBody(byte[] bytes) {
        if (bytes == null) {
            bytes = new byte[0];
        }
        return RequestBody.create(MediaType.parse("multipart/form-data"), bytes);
    }

    /**
     * 构建json请求体
     * 过时了 请直接使用RequestBody.toXXX方
     *
     * @param json
     * @return
     */
    @Deprecated
    public static RequestBody createJsonBody(String json) {
        return RequestBody
                .create(MediaType.parse("application/json; charset=utf-8"),
                        TextUtils.isEmpty(json) ? "" : json);
    }

}
