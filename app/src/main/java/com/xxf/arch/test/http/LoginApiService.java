package com.xxf.arch.test.http;

import com.google.gson.JsonObject;
import com.xxf.arch.annotation.BaseUrl;
import com.xxf.arch.annotation.Interceptor;
import com.xxf.arch.annotation.RxHttpCache;
import com.xxf.arch.annotation.RxHttpCacheProvider;
import com.xxf.arch.annotation.RxJavaInterceptor;
import com.xxf.arch.json.datastructure.ListOrSingle;
import com.xxf.arch.test.Weather;

import io.reactivex.Observable;
import retrofit2.CacheType;
import retrofit2.http.Cache;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;

/**
 * 提供基础路由
 */
@BaseUrl("http://api.map.baidu.com/")

/**
 * 提供缓存目录设置
 */
@RxHttpCacheProvider(DefaultRxHttpCacheDirectoryProvider.class)
/**
 * 声明拦截器
 */
@Interceptor({MyLoggerInterceptor.class, MyLoggerInterceptor2.class})

/**
 * 声明rxJava拦截器
 */
@RxJavaInterceptor(DefaultCallAdapter.class)
public interface LoginApiService {

    /**
     * 声明接口 跟retrofit一致
     *
     * @return
     */
    @GET("telematics/v3/weather?location=%E5%98%89%E5%85%B4&output=json&ak=5slgyqGDENN7Sy7pw29IUvrZ")
    Observable<JsonObject> getCity();

    /**
     * 在retrofit上面扩展了 @Cache 设置缓存类型
     *
     * @param cacheType
     * @return
     */
    @GET("telematics/v3/weather?location=%E5%98%89%E5%85%B4&output=json&ak=5slgyqGDENN7Sy7pw29IUvrZ")
    Observable<ListOrSingle<Weather>> getCity(@Cache CacheType cacheType);

    /**
     * 缓存5s
     * 添加在方法上     @Headers("cache:5000")
     *
     * @param cacheType
     * @return
     */
    @GET("telematics/v3/weather?location=%E5%98%89%E5%85%B4&output=json&ak=5slgyqGDENN7Sy7pw29IUvrZ")
    @Headers("cache:5000")
    Observable<JsonObject> getCity2(@Cache CacheType cacheType);

    /**
     * 缓存
     * 添加在参数上 @Header("cache") long time
     *
     * @param cacheType
     * @return
     */
    @GET("telematics/v3/weather?location=%E5%98%89%E5%85%B4&output=json&ak=5slgyqGDENN7Sy7pw29IUvrZ")
    Observable<JsonObject> getCity3(@Header("cache") long time, @Cache CacheType cacheType);


    @GET("telematics/v3/weather?location=%E5%98%89%E5%85%B4&output=json&ak=5slgyqGDENN7Sy7pw29IUvrZ")
    @RxHttpCache(CacheType.onlyCache)
    Observable<JsonObject> getCityOnlyCache();

}
