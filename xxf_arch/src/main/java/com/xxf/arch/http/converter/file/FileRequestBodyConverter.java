package com.xxf.arch.http.converter.file;

import com.xxf.arch.http.RequestUtils;

import java.io.File;
import java.io.IOException;

import okhttp3.MultipartBody;
import retrofit2.Converter;
import retrofit2.http.PartMap;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class FileRequestBodyConverter<T extends File> implements Converter<T, MultipartBody.Part> {


    @Override
    public MultipartBody.Part convert(T file) throws IOException {
        return MultipartBody.Part.create(RequestUtils.createFormBody(file));
    }
}