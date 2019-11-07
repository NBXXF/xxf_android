package com.xxf.arch.http;

import android.text.TextUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

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
     * 构建表单请求体
     *
     * @param file
     * @return
     */
    public static RequestBody createFileBody(File file) {
        if (file != null && file.exists()) {
            return RequestBody.create(MediaType.parse("multipart/form-data"), file);
        }
        return null;
    }

    /**
     * 构建表单请求体
     *
     * @param file
     * @return
     */
    public static MultipartBody.Part createFileBody(String formKey, File file) {
        if (file != null && file.exists()) {
            return MultipartBody.Part.createFormData(formKey, file.getName(), RequestUtils.createFileBody(file));
        }
        return null;
    }

    /**
     * 构建表单请求体
     *
     * @param formKey
     * @param files
     * @return
     */
    public static Map<String, RequestBody> createFileBody(String formKey, List<File> files) {
        Map<String, RequestBody> fileBodyMaps = new HashMap<>();
        for (File file : files) {
            String key = formKey + "\"; filename=\"" + file.getName();
            fileBodyMaps.put(key, createFileBody(file));
        }
        return fileBodyMaps;
    }

    /**
     * 构建表单请求体
     *
     * @return
     */
    public static RequestBody createFileBody(byte[] bytes) {
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
