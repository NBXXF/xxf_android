package com.xxf.arch.test.http;

import com.xxf.arch.XXF;
import com.xxf.arch.http.interceptor.HttpExceptionFeedInterceptor;

/**
 * @Description: java类作用描述
 * @Author: XGod
 * @CreateDate: 2020/6/16 17:37
 */
public class MyLoggerInterceptor extends HttpExceptionFeedInterceptor {
    public MyLoggerInterceptor() {

    }

    @Override
    protected void onFeedHttpException(String url, String method, Throwable e) {
        XXF.getLogger().d("============>feed ex:" + e);
    }

    @Override
    protected void onFeedResponseException(String url, String method, String reqBody, int code, String message, long tookMs, String resBody) {
        XXF.getLogger().d("============>feed ex:" + url + reqBody);
    }
}
