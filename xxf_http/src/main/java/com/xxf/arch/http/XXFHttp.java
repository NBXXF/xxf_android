package com.xxf.arch.http;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.xxf.arch.annotation.BaseUrl;
import com.xxf.arch.annotation.BaseUrlProvider;
import com.xxf.arch.annotation.GsonInterceptor;
import com.xxf.arch.annotation.RxHttpCacheProvider;
import com.xxf.arch.http.cache.HttpCacheDirectoryProvider;
import com.xxf.arch.http.cache.RxHttpCache;
import com.xxf.arch.http.converter.gson.GsonConvertInterceptor;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Interceptor;

/**
 * @author xuanyouwu@163.com
 * @version 2.3.1
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
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
        return (T) api;
    }

    /**
     * remove api service
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
        if (baseUrlAnnotation == null&&baseUrlProviderAnnotation==null) {
            throw new IllegalArgumentException("please use  BaseUrl or BaseUrlProvider Annotation to" + apiClazz);
        }
        String baseUrl;
        if(baseUrlProviderAnnotation!=null) {
            baseUrl= baseUrlProviderAnnotation.value().newInstance().getBaseUrl(apiClazz);
        }else {
            baseUrl=baseUrlAnnotation.value();
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

        //解析器拦截可选
        GsonInterceptor gsonInterceptorAnnotation = apiClazz.getAnnotation(GsonInterceptor.class);
        GsonConvertInterceptor gsonConvertInterceptor = null;
        if (gsonInterceptorAnnotation != null) {
            gsonConvertInterceptor = gsonInterceptorAnnotation.value().newInstance();
        }

        //rxJava 缓存
        RxHttpCache rxHttpCache = null;
        RxHttpCacheProvider rxHttpCacheAnnotation = apiClazz.getAnnotation(RxHttpCacheProvider.class);
        if (rxHttpCacheAnnotation != null) {
            HttpCacheDirectoryProvider rxHttpCacheDirectoryProvider = rxHttpCacheAnnotation.value().newInstance();
            rxHttpCache = new RxHttpCache(new File(rxHttpCacheDirectoryProvider.getDirectory()), rxHttpCacheDirectoryProvider.maxSize());
        }

        //创建缓存对象
        T apiService = new RetrofitBuilder(gsonConvertInterceptor, rxHttpCache)
                .client(ohcb.build())
                .baseUrl(baseUrl)
                .build()
                .create(apiClazz);
        return apiService;
    }
}
