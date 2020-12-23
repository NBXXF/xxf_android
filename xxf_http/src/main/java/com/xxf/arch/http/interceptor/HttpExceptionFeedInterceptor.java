package com.xxf.arch.http.interceptor;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description 专注日常http异常汇报, 由于header太多, 日志系统显示不全, 所以汇报精简日志
 */
public abstract class HttpExceptionFeedInterceptor implements Interceptor {
    public static final Charset UTF8 = Charset.forName("UTF-8");

    @NotNull
    @Override
    public final Response intercept(@NotNull Interceptor.Chain chain) throws IOException {
        long startNs = System.nanoTime();
        Request request = chain.request();
        Response response;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            onFeedHttpException(String.valueOf(request.url()), request.method(), e);
            throw e;
        }
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
        //汇报异常
        if (response != null
                && !response.isSuccessful()) {
            onFeedResponseException(
                    String.valueOf(request.url()),
                    request.method(),
                    getLoggerRequestBody(request.body()),
                    response.code(),
                    response.message(),
                    tookMs,
                    getLoggerResponseBody(response.body()));
        }
        return response;
    }

    /**
     * 异常汇报
     *
     * @param url
     * @param method
     * @param e
     */
    protected abstract void onFeedHttpException(String url, String method, Throwable e);

    /**
     * 响应异常汇报
     *
     * @param url
     * @param method
     * @param reqBody
     * @param code
     * @param message
     * @param tookMs  消耗时间
     * @param resBody
     */
    protected abstract void onFeedResponseException(String url, String method, String reqBody, int code, String message, long tookMs, String resBody);

    /**
     * 获取 响应体
     *
     * @param responseBody
     * @return
     */
    public static String getLoggerResponseBody(ResponseBody responseBody) {
        if (responseBody != null) {
            try {
                long contentLength = responseBody.contentLength();

                BufferedSource source = responseBody.source();
                source.request(Long.MAX_VALUE); // Buffer the entire body.
                Buffer buffer = source.buffer();

                Charset charset = UTF8;
                MediaType contentType = responseBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(UTF8);
                }

                if (!HttpLoggingInterceptor.isPlaintext(buffer)) {
                    return "binary " + buffer.size() + "-byte body omitted";
                }

                if (contentLength != 0) {
                    return buffer.clone().readString(charset);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取请求体
     *
     * @param requestBody
     * @return
     */
    public static String getLoggerRequestBody(RequestBody requestBody) {
        if (requestBody != null) {
            try {
                Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);

                Charset charset = UTF8;
                MediaType contentType = requestBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(UTF8);
                }

                if (HttpLoggingInterceptor.isPlaintext(buffer)) {
                    return buffer.readString(charset);
                } else {
                    return "binary " + requestBody.contentLength() + "-byte body omitted";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
