package com.xxf.http.demo;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.xxf.arch.annotation.BaseUrl;
import com.xxf.arch.annotation.Dispatcher;
import com.xxf.arch.annotation.Interceptor;
import com.xxf.arch.annotation.JsonString;
import com.xxf.arch.annotation.RxHttpCacheConfig;
import com.xxf.arch.annotation.RxJavaInterceptor;
import com.nbxxf.kpower.json.datastructure.ListOrSingle;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.CacheType;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import retrofit2.http.Tag;

/**
 * 提供基础路由
 */
@BaseUrl("http://api.map.baidu.com/")

/**
 * 指定并发调度
 */
@Dispatcher(maxRequests = 1,maxRequestsPerHost = 2)

/**
 * 提供缓存目录设置
 */
@RxHttpCacheConfig(DefaultRxHttpCacheDirectoryProvider.class)
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
     * 在retrofit上面扩展了 @Tag 设置缓存类型
     *
     * @param cacheType
     * @return
     */
    @GET("telematics/v3/weather?location=%E5%98%89%E5%85%B4&output=json&ak=5slgyqGDENN7Sy7pw29IUvrZ")
    Observable<ListOrSingle<Weather>> getCity(@Tag CacheType cacheType);

    /**
     * 在retrofit上面扩展了 @Tag 设置缓存类型
     *
     * @param cacheType
     * @return
     */
    @GET("telematics/v3/weather?location=%E5%98%89%E5%85%B4&output=json&ak=5slgyqGDENN7Sy7pw29IUvrZ")
    Observable<BaseResponseDTO> getCityModel(@Tag CacheType cacheType);

    /**
     * 模式一: 固定缓存时间.
     * 在方法上通过 @Headers("cache:5000") 设置缓存时间, 单位毫秒.
     */
    @GET("telematics/v3/weather?location=%E5%98%89%E5%85%B4&output=json&ak=5slgyqGDENN7Sy7pw29IUvrZ")
    @Headers("cache:5000")
    Observable<JsonObject> getCityWithFixedCacheTime(@Tag CacheType cacheType);

    /**
     * 模式二: 动态缓存时间.
     * 在参数上通过 @Header("cache") long cacheTime 设置缓存时间, 单位毫秒.
     */
    @GET("telematics/v3/weather?location=%E5%98%89%E5%85%B4&output=json&ak=5slgyqGDENN7Sy7pw29IUvrZ")
    Observable<JsonObject> getCityWithDynamicCacheTime(@Header("cache") long cacheTime, @Tag CacheType cacheType);


    @GET("telematics/v3/weather?location=%E5%98%89%E5%85%B4&output=json&ak=5slgyqGDENN7Sy7pw29IUvrZ")
    Observable<JsonObject> getCityOnlyCache();


    @Headers(value ="XXX:476745")
    @GET("telematics/v3/weather?location=%E5%98%89%E5%85%B4&output=json&ak=5slgyqGDENN7Sy7pw29IUvrZ")
    Observable<JsonArray> getCity(@JsonString @Query("test") TestQueryJsonField queryJsonField);

}
