package com.xxf.arch.http.cache;

import com.xxf.arch.XXF;
import com.xxf.arch.utils.NetUtils;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author youxuan  E-mail:youxuan@icourt.cc
 * @version 2.4.0
 * @Description 没有网 强制读取缓存 需要结合{@link com.xxf.arch.annotation.OkHttpCacheProvider}
 * @date createTime：2018/8/15
 */
public class NoNetReadCacheInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        /**
         * 没有网 强制读取缓存
         */
        Request request = chain.request();
        if ("GET".equals(request.method()) && !NetUtils.hasNetwork(XXF.getApplication())) {
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
            Response forceCacheResponse = chain.proceed(request);
            //504 代表找不到对应的资源
            if (!(forceCacheResponse.code() == 504)) {
                return forceCacheResponse;
            }
        }
        return chain.proceed(request
                .newBuilder()
                .cacheControl(CacheControl.FORCE_NETWORK)
                .build());
    }
}
