package com.xxf.arch.test.http;

import com.google.gson.JsonObject;
import com.xxf.arch.annotation.BaseUrl;
import com.xxf.arch.annotation.RxCache;

import io.reactivex.Observable;
import retrofit2.http.GET;

@BaseUrl("http://api.map.baidu.com/")
public interface LoginApiService {

    @GET("http://api.map.baidu.com/telematics/v3/weather?location=%E5%98%89%E5%85%B4&output=json&ak=5slgyqGDENN7Sy7pw29IUvrZ")
    @RxCache(RxCache.CacheType.firstCache)
    Observable<JsonObject> getCity();

}
