package com.xxf.arch.http;


import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.xxf.arch.http.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.xxf.arch.http.adapter.rxjava2.RxJavaCallAdapterInterceptor;
import com.xxf.arch.http.cache.HttpCacheConfigProvider;
import com.xxf.arch.http.converter.gson.GsonConverterFactory;
import com.xxf.arch.http.converter.json.JsonConverterFactory;
import com.xxf.arch.http.converter.json.JsonStringConverterFactory;
import com.xxf.arch.http.converter.string.ScalarsConverterFactory;
import com.xxf.arch.json.GsonFactory;
import com.xxf.arch.json.exclusionstrategy.ExposeDeserializeExclusionStrategy;
import com.xxf.arch.json.exclusionstrategy.ExposeSerializeExclusionStrategy;

import java.util.List;
import java.util.concurrent.Executor;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
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
 * @version 2.3.1
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
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

    public RetrofitBuilder(RxJavaCallAdapterInterceptor interceptor, HttpCacheConfigProvider rxHttpCache) {
        GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create(
                //网络层一定要去除 expose  serialize  = false  或者deserialize  = false 的情况
                GsonFactory.createGson(true, true));
        builder = new Retrofit.Builder()
                .client(new OkHttpClientBuilder().build())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(gsonConverterFactory)
                .addConverterFactory(JsonConverterFactory.create())
                .addConverterFactory(new JsonStringConverterFactory(gsonConverterFactory))
                .addCallAdapterFactory(new RxJava2CallAdapterFactory(null, true, rxHttpCache, interceptor));
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
