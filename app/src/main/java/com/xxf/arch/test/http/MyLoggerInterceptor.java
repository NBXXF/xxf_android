package com.xxf.arch.test.http;

import com.xxf.arch.XXF;
import com.xxf.arch.http.interceptor.XXFHttpLoggingInterceptor;

import java.io.IOException;

import okhttp3.Response;

/**
 * @Description: java类作用描述
 * @Author: XGod
 * @CreateDate: 2020/6/16 17:37
 */
public class MyLoggerInterceptor extends XXFHttpLoggingInterceptor {

    StringBuilder stringBuilder = new StringBuilder();


    public MyLoggerInterceptor() {
        this.setLogger(new Logger() {
            @Override
            public void log(String message) {
                stringBuilder.
                        append("\n")
                        .append(message);
                XXF.getLogger().d(message);
            }
        });
        this.setLevel(Level.BODY);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        stringBuilder = new StringBuilder();
        Response intercept = super.intercept(chain);
        XXF.getLogger().d("yes:" + stringBuilder.toString());
        return intercept;
    }
}
