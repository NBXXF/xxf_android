package com.xxf.arch.test.http;

import android.util.Log;

import com.xxf.arch.XXF;

import java.io.IOException;

import okhttp3.Response;

/**
 * @Description: java类作用描述
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/6/16 17:37
 */
public class MyLoggerInterceptor2 extends com.xxf.arch.http.interceptor.HttpLoggingInterceptor {
    public MyLoggerInterceptor2() {
        super(new Logger() {

            @Override
            public void log(String message) {
                Log.d("http:",message);
            }
        });
        setLevel(Level.BODY);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        return super.intercept(chain);
    }
}
