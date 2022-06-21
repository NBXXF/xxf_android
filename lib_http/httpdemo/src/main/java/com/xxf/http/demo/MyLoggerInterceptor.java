package com.xxf.http.demo;

import android.util.Log;

import com.xxf.arch.http.interceptor.HttpExceptionFeedInterceptor;

import org.jetbrains.annotations.NotNull;

import okhttp3.Request;

/**
 * @Description: java类作用描述
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/6/16 17:37
 */
public class MyLoggerInterceptor extends HttpExceptionFeedInterceptor {
    public MyLoggerInterceptor() {

    }

    @Override
    public void onFeedHttpException(@NotNull Request request, @NotNull String string) {
        super.onFeedHttpException(request, string);
        System.out.println("============>feed ex2:"+ string);
        Log.d("","============>feed ex:"+ string);
    }
}
