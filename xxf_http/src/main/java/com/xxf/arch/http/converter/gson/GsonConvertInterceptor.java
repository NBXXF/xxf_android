package com.xxf.arch.http.converter.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.io.IOException;

import okhttp3.ResponseBody;


/**
 * json转换拦截器
 *
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description json转换拦截器
 */
public interface GsonConvertInterceptor {

    /**
     * 转换拦截
     * 转换自定义如下
     * <p>
     * JsonReader jsonReader = gson.newJsonReader(value.charStream());
     * //宽容解析
     * jsonReader.setLenient(true);
     * try {
     * T result = adapter.read(jsonReader);
     * if (jsonReader.peek() != JsonToken.END_DOCUMENT) {
     * throw new JsonIOException("JSON document was not fully consumed.");
     * }
     * return result;
     * } finally {
     * value.close();
     * }
     *
     * @param gson
     * @param adapter
     * @param <T>
     * @return
     * @throws IOException
     */
    <T> T onResponseBodyIntercept(Gson gson, TypeAdapter<T> adapter, ResponseBody value) throws IOException;

}
