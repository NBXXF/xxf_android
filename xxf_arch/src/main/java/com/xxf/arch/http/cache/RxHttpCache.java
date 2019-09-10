
package com.xxf.arch.http.cache;


import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.xxf.arch.http.cache.disklrucache.SimpleDiskLruCache;
import com.xxf.arch.json.JsonUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import io.reactivex.annotations.Nullable;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.ResponseBody;
import okio.ByteString;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description rewrite by xxf  no final
 * <p>
 * 目前只支持json
 */
@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class RxHttpCache {
    private static final int VERSION = 201105;
    private SimpleDiskLruCache cache;

    public RxHttpCache(File directory, long maxSize) {
        try {
            cache = SimpleDiskLruCache.open(directory, VERSION, maxSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String key(HttpUrl url) {
        return ByteString.encodeUtf8(url.toString()).md5().hex();
    }

    @Nullable
    public <T> Response<T> get(Request request, Converter<ResponseBody, T> responseConverter) {
        if (cache == null) {
            return null;
        }
        String requestMethod = request.method();
        if (requestMethod.equals("GET")) {
            String key = key(request.url());
            SimpleDiskLruCache.StringEntry stringEntry = null;
            try {
                stringEntry = cache.getString(key);
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
        }
        return null;
    }

    @Nullable
    public <T> void put(Response<T> response) {
        if (cache == null) {
            return;
        }
        String requestMethod = response.raw().request().method();
        String contentType = response.headers().get("Content-Type");
        if (requestMethod.equals("GET")
                && contentType != null
                && contentType.toLowerCase().startsWith("application/json")) {
            Request request = response.raw().request();
            String key = key(request.url());
            try {
                cache.put(key, JsonUtils.toJsonString(response.body()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
