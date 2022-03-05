
package com.xxf.arch.http.cache;


import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.xxf.arch.http.cache.disklrucache.SimpleDiskLruCache;
import com.xxf.arch.http.interceptor.HttpLoggingInterceptor;
import com.xxf.arch.json.JsonUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.ByteString;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description rewrite by xxf  no final
 * <p>
 * 目前只支持json
 */
@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class RxHttpCache {
    private static final int VERSION = 201105;
    private SimpleDiskLruCache diskLruCache;
    private static final String CACHE_PREFIX = "http_prefix_";

    public RxHttpCache(File directory, long maxSize) {
        try {
            diskLruCache = SimpleDiskLruCache.open(directory, VERSION, maxSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String key(HttpUrl url) {
        return ByteString.encodeUtf8(url.toString()).md5().hex();
    }

    public static String key(String key) {
        return ByteString.encodeUtf8(key).md5().hex();
    }

    @Nullable
    public <T> Response<T> get(Request request, Converter<ResponseBody, T> responseConverter, long cacheTime) {
        if (diskLruCache == null) {
            return null;
        }
        String requestMethod = request.method();
        if (TextUtils.equals(requestMethod, "GET")) {
            String key = key(request.url());
            if (checkCache(key, cacheTime)) {
                return getResponseByKey(key, responseConverter);
            }
        } else if (requestMethod.equals("POST")) {
            try {
                Buffer buffer = new Buffer();
                request.body().writeTo(buffer);

                Charset charset = StandardCharsets.UTF_8;
                MediaType reqContentType = request.body().contentType();
                if (reqContentType != null) {
                    charset = reqContentType.charset(StandardCharsets.UTF_8);
                }
                if (HttpLoggingInterceptor.isPlaintext(buffer)) {
                    String key = key(request.url() + buffer.clone().readString(charset));
                    if (checkCache(key, cacheTime)) {
                        return getResponseByKey(key, responseConverter);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * @param urlKey
     * @return
     */
    private boolean checkCache(String urlKey, long cacheTime) {
        if (diskLruCache != null && cacheTime >= 0) {
            try {
                String recordTimeKey = CACHE_PREFIX + urlKey;
                SimpleDiskLruCache.StringEntry recordTimeEntry = diskLruCache.getString(recordTimeKey);
                long lastCacheTime = Long.parseLong(recordTimeEntry.getString());
                return (lastCacheTime + cacheTime) > System.currentTimeMillis();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 记录缓存时间
     *
     * @param urlKey
     */
    private void recordCacheTime(String urlKey) {
        if (diskLruCache != null) {
            String recordTimeKey = CACHE_PREFIX + urlKey;
            try {
                diskLruCache.put(recordTimeKey, String.valueOf(System.currentTimeMillis()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取缓存
     *
     * @param key
     * @param responseConverter
     * @param <T>
     * @return
     */
    private <T> Response<T> getResponseByKey(String key, Converter<ResponseBody, T> responseConverter) {
        SimpleDiskLruCache.StringEntry stringEntry = null;
        try {
            stringEntry = diskLruCache.getString(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (stringEntry == null) {
            return null;
        }
        String jsonCache = stringEntry.getString();
        if (TextUtils.isEmpty(jsonCache)) {
            return null;
        }
        final MediaType MEDIA_TYPE = MediaType.get("application/json; charset=UTF-8");
        try {
            return Response.success(responseConverter.convert(ResponseBody.create(MEDIA_TYPE, jsonCache)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 异步缓存
     *
     * @param response
     * @param <T>
     */
    @Nullable
    public <T> void putAsync(final Response<T> response) {
        Observable
                .fromCallable(new Callable<Object>() {
                    @Override
                    public Object call() throws Exception {
                        put(response);
                        return true;
                    }
                })
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    @Nullable
    public <T> void put(Response<T> response) {
        if (diskLruCache == null) {
            return;
        }
        String requestMethod = response.raw().request().method();
        String contentType = response.headers().get("Content-Type");
        /**
         * 只缓存json
         * 且是成功状态
         */
        boolean supportContentCache = contentType != null && contentType.toLowerCase().startsWith("application/json") && response.isSuccessful();
        if (supportContentCache) {
            Request request = response.raw().request();
            if (TextUtils.equals(requestMethod, "GET")) {
                String key = key(request.url());
                try {
                    diskLruCache.put(key, JsonUtils.toJsonString(response.body(),true));
                    recordCacheTime(key);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestMethod.equals("POST")) {
                try {
                    Buffer buffer = new Buffer();
                    request.body().writeTo(buffer);

                    Charset charset = StandardCharsets.UTF_8;
                    MediaType reqContentType = request.body().contentType();
                    if (reqContentType != null) {
                        charset = reqContentType.charset(StandardCharsets.UTF_8);
                    }
                    if (HttpLoggingInterceptor.isPlaintext(buffer)) {
                        String key = key(request.url() + buffer.clone().readString(charset));
                        try {
                            diskLruCache.put(key, JsonUtils.toJsonString(response.body(),true));
                            recordCacheTime(key);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
