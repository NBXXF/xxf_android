package com.xxf.arch.http.converter.json;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class JsonConverterFactory extends Converter.Factory {

    public static JsonConverterFactory create() {
        return new JsonConverterFactory();
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        Class<?> rawType = getRawType(type);
        if (rawType == JSONObject.class) {
            return new JsonResponseBodyConverter<JSONObject>();
        } else if (rawType == JSONArray.class) {
            return new JsonResponseBodyConverter<JSONArray>();
        }
        //不能解析就返回为空
        return null;
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        Class<?> rawType = getRawType(type);
        if (rawType == JSONObject.class) {
            return new JsonRequestBodyConverter<JSONObject>();
        } else if (rawType == JSONArray.class) {
            return new JsonRequestBodyConverter<JSONArray>();
        }
        //不能解析就返回为空
        return null;
    }
}
