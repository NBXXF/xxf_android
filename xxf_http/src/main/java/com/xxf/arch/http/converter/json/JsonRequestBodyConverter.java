package com.xxf.arch.http.converter.json;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;
import java.io.IOException;
/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
final class JsonRequestBodyConverter<T> implements Converter<T, RequestBody> {
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");

    JsonRequestBodyConverter() {

    }

    @Override
    public RequestBody convert(T value) throws IOException {
        return RequestBody.create(MEDIA_TYPE, value.toString());
    }
}
