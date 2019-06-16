package com.xxf.arch.http.converter.file;

import java.io.File;
import java.lang.annotation.Annotation;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

import java.lang.reflect.Type;
import java.util.List;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class FileConverterFactory extends Converter.Factory {

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        Class<?> rawType = getRawType(type);
        if (rawType == File.class) {
            return new FileRequestBodyConverter();
        } else if (rawType == List.class) {
            return new FileListRequestBodyConverter();
        }
        //解析不了,返回null
        return null;
    }

}