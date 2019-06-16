package com.xxf.arch.http;

import android.text.TextUtils;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Description
 * <p>
 * author  youxuan  E-mail:xuanyouwu@163.com
 * date createTime：2017/4/10
 * version 1.0.0
 */
public class RequestUtils {

    /**
     * 构建文本请求体
     *
     * @param text
     * @return
     */
    public static RequestBody createTextBody(String text) {
        return RequestBody
                .create(MediaType.parse("text/plain"),
                        TextUtils.isEmpty(text) ? "" : text);
    }

    /**
     * 构建图片请求体
     *
     * @param file
     * @return
     */
    public static RequestBody createImgBody(File file) {
        if (file != null && file.exists()) {
            return RequestBody.create(MediaType.parse("image/*"), file);
        }
        return null;
    }

    /**
     * 构建媒体流请求key
     *
     * @param file
     * @return
     */
    public static String createStreamKey(File file) {
        if (file != null && file.exists()) {
            return "file\"; filename=\"" + file.getName();
        }
        return "file\"; filename=\"";
    }

    /**
     * 构建媒体流请求key
     *
     * @param fileName
     * @return
     */
    public static String createStreamKey(String fileName) {
        return "file\"; filename=\"" + fileName;
    }


    /**
     * 构建媒体流请求key
     *
     * @param fileName
     * @return
     */
    public static String createStreamKey(String description, String fileName) {
        return description + "\"; filename=\"" + fileName;
    }

    /**
     * 构建表单请求体
     *
     * @param file
     * @return
     */
    public static RequestBody createFormBody(File file) {
        if (file != null && file.exists()) {
            return RequestBody.create(MediaType.parse("multipart/form-data"), file);
        }
        return null;
    }

    /**
     * 构建表单请求体
     *
     * @return
     */
    public static RequestBody createFormBody(byte[] bytes) {
        if (bytes == null) {
            bytes = new byte[0];
        }
        return RequestBody.create(MediaType.parse("multipart/form-data"), bytes);

    }

    /**
     * 构建json请求体
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
