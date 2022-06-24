package com.xxf.http.demo;

import android.util.Log;

import com.xxf.arch.http.interceptor.HttpExceptionFeedInterceptor;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import okhttp3.Request;
import okhttp3.Response;

/**
 * @Description: java类作用描述
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/6/16 17:37
 */
public class MyLoggerInterceptor extends HttpExceptionFeedInterceptor {
    public MyLoggerInterceptor() {

    }

    @Override
    public void onFeedHttpException(@NotNull Request request, @Nullable Response response, @Nullable Throwable throwable, long tookMs, @NotNull String resultLog) {
        super.onFeedHttpException(request, response, throwable, tookMs, resultLog);
        System.out.println("============>feed ex2:"+ resultLog);
        Log.d("","============>feed ex:"+ resultLog);
    }
}
