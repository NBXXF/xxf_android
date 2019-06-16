package com.xxf.arch.http;

import android.support.annotation.Nullable;

import com.xxf.arch.http.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.xxf.arch.http.converter.gson.GsonConvertInterceptor;
import com.xxf.arch.http.converter.gson.GsonConverterFactory;
import com.xxf.arch.http.converter.json.JsonConverterFactory;
import com.xxf.arch.json.GsonFactory;

import java.util.List;
import java.util.concurrent.Executor;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

import com.xxf.arch.http.cache.RxHttpCache;

import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
/**
 * 默认实例
 * 1.解析层
 * 2.rxjava
 * 3.默认的okhttp
 * 4.rxjava 支持缓存
 */

/**
 * @author xuanyouwu@163.com
 * @version 2.3.1
 * @Description
 * @date createTime：2018/9/7
 */
public class RetrofitBuilder {
    private static <T> T checkNotNull(@Nullable T object, String message) {
        if (object == null) {
            throw new NullPointerException(message);
        }
        return object;
    }

    /**
     * 默认实例
     * 1.解析层
     * 2.rxjava
     * 3.默认的okhttp
     * 4.rxjava 支持缓存
     */
    protected Retrofit.Builder builder;

    public RetrofitBuilder(GsonConvertInterceptor interceptor, RxHttpCache rxHttpCache) {
        builder = new Retrofit.Builder()
                .client(new OkHttpClientBuilder().build())
                .addConverterFactory(GsonConverterFactory.create(GsonFactory.createGson(), interceptor))
                .addConverterFactory(JsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync(rxHttpCache));
    }


    public RetrofitBuilder client(OkHttpClient client) {
        return callFactory(checkNotNull(client, "client == null"));
    }


    public RetrofitBuilder callFactory(okhttp3.Call.Factory factory) {
        builder.callFactory(factory);
        return this;
    }


    public RetrofitBuilder baseUrl(String baseUrl) {
        checkNotNull(baseUrl, "baseUrl == null");
        builder.baseUrl(HttpUrl.get(baseUrl));
        return this;
    }


    public RetrofitBuilder baseUrl(HttpUrl baseUrl) {
        builder.baseUrl(baseUrl);
        return this;
    }

    public RetrofitBuilder addConverterFactory(Converter.Factory factory) {
        builder.addConverterFactory(factory);
        return this;
    }


    public RetrofitBuilder addCallAdapterFactory(CallAdapter.Factory factory) {
        builder.addCallAdapterFactory(factory);
        return this;
    }

    public RetrofitBuilder callbackExecutor(Executor executor) {
        builder.callbackExecutor(executor);
        return this;
    }

    public List<CallAdapter.Factory> callAdapterFactories() {
        return builder.callAdapterFactories();
    }

    public List<Converter.Factory> converterFactories() {
        return builder.converterFactories();
    }

    public RetrofitBuilder validateEagerly(boolean validateEagerly) {
        builder.validateEagerly(validateEagerly);
        return this;
    }

    public Retrofit build() {
        return builder.build();
    }
}
