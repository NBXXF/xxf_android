package com.xxf.arch.test.http;

import com.google.gson.JsonObject;
import com.xxf.arch.annotation.BaseUrl;
import com.xxf.arch.annotation.Interceptor;
import com.xxf.arch.annotation.OkHttpCacheProvider;
import com.xxf.arch.annotation.RxHttpCache;
import com.xxf.arch.annotation.RxHttpCacheProvider;
import com.xxf.arch.http.cache.NoNetReadCacheInterceptor;
import com.xxf.arch.http.cache.DefaultOkHttpCacheDirectoryProvider;
import com.xxf.arch.http.cache.DefaultRxHttpCacheDirectoryProvider;

import io.reactivex.Observable;
import retrofit2.http.GET;

@BaseUrl("http://api.map.baidu.com/")
@RxHttpCacheProvider(DefaultRxHttpCacheDirectoryProvider.class)
@Interceptor(NoNetReadCacheInterceptor.class)
@OkHttpCacheProvider(DefaultOkHttpCacheDirectoryProvider.class)
public interface LoginApiService {

    @GET("http://api.map.baidu.com/telematics/v3/weather?location=%E5%98%89%E5%85%B4&output=json&ak=5slgyqGDENN7Sy7pw29IUvrZ")
    @RxHttpCache(RxHttpCache.CacheType.firstCache)
    Observable<JsonObject> getCity();

}
