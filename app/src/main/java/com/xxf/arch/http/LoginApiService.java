package com.xxf.arch.http;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface LoginApiService {

    @GET("http://api.map.baidu.com/telematics/v3/weather?location=%E5%98%89%E5%85%B4&output=json&ak=5slgyqGDENN7Sy7pw29IUvrZ")
    Observable<JsonObject> getCity();

}
