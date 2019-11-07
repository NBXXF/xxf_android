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
     *
     * @param gson
     * @param adapter
     * @param convertedBody 默认转换好的的body
     * @param <T>
     * @return
     * @throws IOException
     */
    <T> T onResponseBodyIntercept(Gson gson, TypeAdapter<T> adapter, ResponseBody value, T convertedBody) throws IOException;

}
