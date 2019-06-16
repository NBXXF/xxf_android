package com.xxf.arch.http.converter.file;

import com.xxf.arch.http.RequestUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Converter;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class FileListRequestBodyConverter<T extends List<File>> implements Converter<T, Map<String, RequestBody>> {

    @Override
    public Map<String, RequestBody> convert(T value) throws IOException {
        Map<String, RequestBody> requestBodyMap = new HashMap<>();
        for (File file : value) {
            requestBodyMap.put(RequestUtils.createStreamKey(file), RequestUtils.createFormBody(file));
        }
        return requestBodyMap;
    }
}