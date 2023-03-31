package com.xxf.arch.http;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.xxf.arch.annotation.BaseUrl;
import com.xxf.arch.annotation.BaseUrlProvider;
import com.xxf.arch.annotation.CookieJar;
import com.xxf.arch.annotation.Dispatcher;
import com.xxf.arch.annotation.Dns;
import com.xxf.arch.annotation.RxHttpCacheConfig;
import com.xxf.arch.annotation.RxJavaInterceptor;
import com.xxf.arch.http.adapter.rxjava2.RxJavaCallAdapterInterceptor;
import com.xxf.arch.http.cache.HttpCacheConfigProvider;

import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.rxjava3.disposables.Disposable;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

/**
 * @version 2.3.1
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description
 * @date createTime：2018/9/7
 */
@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class XXFHttp {

    private static final ConcurrentHashMap<Class, Object> API_MAP = new ConcurrentHashMap<>();

    /**
     * get api
     *
     * @param apiClazz
     * @param <T>
     * @return
     */
    public static <T> T getApiService(Class<T> apiClazz) {
        Object api = API_MAP.get(apiClazz);
        if (api == null) {
            try {
                API_MAP.put(apiClazz, api = createApiService(apiClazz));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return (T) api;
    }

    /**
     * remove api service
     *
     * @param apiClazz
     */
    public static void clearApiService(Class apiClazz) {
        API_MAP.remove(apiClazz);
    }

    /**
     * remove all api service
     */
    public static void clearAllApiService() {
        API_MAP.clear();
    }


    /**
     * @param apiClazz
     * @param <T>
     * @return
     */
    private static <T> T createApiService(Class<T> apiClazz)
            throws IllegalAccessException, InstantiationException, IllegalArgumentException {
        OkHttpClientBuilder ohcb = new OkHttpClientBuilder();

        BaseUrl baseUrlAnnotation = apiClazz.getAnnotation(BaseUrl.class);
        BaseUrlProvider baseUrlProviderAnnotation = apiClazz.getAnnotation(BaseUrlProvider.class);
        if (baseUrlAnnotation == null && baseUrlProviderAnnotation == null) {
            throw new IllegalArgumentException("please use  BaseUrl or BaseUrlProvider Annotation to" + apiClazz);
        }
        String baseUrl;
        if (baseUrlProviderAnnotation != null) {
            baseUrl = baseUrlProviderAnnotation.value().newInstance().getBaseUrl(apiClazz);
        } else {
            baseUrl = baseUrlAnnotation.value();
        }

        /**
         * 设置cookie
         */
        CookieJar cookieJarAnnotation = apiClazz.getAnnotation(CookieJar.class);
        if (cookieJarAnnotation != null && cookieJarAnnotation.value() != null) {
            okhttp3.CookieJar cookieJar = cookieJarAnnotation.value().newInstance();
            if (cookieJar != null) {
                ohcb.cookieJar(cookieJar);
            }
        }

        /**
         * 设置dns
         */
        Dns dnsAnnotation = apiClazz.getAnnotation(Dns.class);
        if (dnsAnnotation  != null && dnsAnnotation .value() != null) {
            okhttp3.Dns dns = dnsAnnotation.value().newInstance();
            if (dns != null) {
                ohcb.dns(dns);
            }
        }

        //拦截器可选
        com.xxf.arch.annotation.Interceptor interceptorAnnotation = apiClazz.getAnnotation(com.xxf.arch.annotation.Interceptor.class);
        if (interceptorAnnotation != null) {
            Class<? extends Interceptor>[] value = interceptorAnnotation.value();
            if (value != null) {
                for (Class<? extends Interceptor> in : value) {
                    Interceptor interceptor = in.newInstance();
                    ohcb.addInterceptor(interceptor);
                }
            }
        }

        //网络拦截器
        com.xxf.arch.annotation.NetworkInterceptor networkInterceptorAnnotation = apiClazz.getAnnotation(com.xxf.arch.annotation.NetworkInterceptor.class);
        if (networkInterceptorAnnotation != null) {
            Class<? extends Interceptor>[] value = networkInterceptorAnnotation.value();
            if (value != null) {
                for (Class<? extends Interceptor> in : value) {
                    Interceptor interceptor = in.newInstance();
                    ohcb.addNetworkInterceptor(interceptor);
                }
            }
        }


        //解析器拦截可选
        RxJavaInterceptor rxJavaInterceptorAnnotation = apiClazz.getAnnotation(RxJavaInterceptor.class);
        RxJavaCallAdapterInterceptor rxJavaInterceptor = null;
        if (rxJavaInterceptorAnnotation != null) {
            rxJavaInterceptor = rxJavaInterceptorAnnotation.value().newInstance();
        }

        //rxJava 缓存
        HttpCacheConfigProvider rxHttpCacheDirectoryProvider = null;
        RxHttpCacheConfig rxHttpCacheAnnotation = apiClazz.getAnnotation(RxHttpCacheConfig.class);
        if (rxHttpCacheAnnotation != null) {
            rxHttpCacheDirectoryProvider = rxHttpCacheAnnotation.value().newInstance();
        }

        OkHttpClient build = ohcb.build();
        Dispatcher dispatcher = apiClazz.getAnnotation(Dispatcher.class);
        if (dispatcher != null) {
            build.dispatcher().setMaxRequests(dispatcher.maxRequests());
            build.dispatcher().setMaxRequests(dispatcher.maxRequestsPerHost());
        }

        //创建缓存对象
        T apiService = new RetrofitBuilder(rxJavaInterceptor, rxHttpCacheDirectoryProvider)
                .client(build)
                .baseUrl(baseUrl)
                .build()
                .create(apiClazz);
        return apiService;
    }

    /**
     * 取消一个订阅
     *
     * @param subscribe
     */
    public static void dispose(Disposable subscribe) {
        if (subscribe != null && !subscribe.isDisposed()) {
            try {
                subscribe.dispose();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
