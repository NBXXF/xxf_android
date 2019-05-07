package com.xxf.arch.http;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.xxf.arch.http.converter.gson.GsonConvertInterceptor;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Interceptor;

/**
 * @author xuanyouwu@163.com
 * @version 2.3.1
 * @Description
 * @date createTime：2018/9/7
 */
public class XXFHttp {

    private static final ConcurrentHashMap<Class, Object> API_MAP = new ConcurrentHashMap<>();

    /**
     * get api
     *
     * @param c
     * @param <T>
     * @return
     */
    public static <T> T getApiService(Class<T> c) {
        return (T) API_MAP.get(c);
    }

    /**
     * register api
     *
     * @param c
     * @param baseUrl
     * @param interceptors
     * @param <T>
     */
    public static <T> void registerApiService(@NonNull Class<T> c,
                                              @NonNull String baseUrl,
                                              @Nullable List<Interceptor> interceptors,
                                              GsonConvertInterceptor gsonConvertInterceptor) {
        OkHttpClientBuilder ohcb = new OkHttpClientBuilder();
        if (interceptors != null) {
            for (Interceptor interceptor : interceptors) {
                ohcb.addInterceptor(interceptor);
            }
        }
        T apiService = new RetrofitBuilder(gsonConvertInterceptor)
                .client(ohcb.build())
                .baseUrl(baseUrl)
                .build()
                .create(c);
        API_MAP.put(c, apiService);
    }
}
