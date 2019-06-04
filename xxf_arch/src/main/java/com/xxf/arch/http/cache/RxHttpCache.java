
package com.xxf.arch.http.cache;


import android.os.Build;
import android.support.annotation.RequiresApi;

import com.google.gson.Gson;

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
 * 可以继承 和公开 get方法 需要结合@{@link com.xxf.arch.annotation.RxHttpCacheProvider}
 * 目前只支持json
 */
@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class RxHttpCache {

    private HttpCacheDirectoryProvider httpCacheDirectoryProvider;

    public RxHttpCache(HttpCacheDirectoryProvider rxCacheProvider) {
        this.httpCacheDirectoryProvider = rxCacheProvider;
    }

    public static String key(HttpUrl url) {
        return ByteString.encodeUtf8(url.toString()).md5().hex();
    }

    @Nullable
    public <T> Response<T> get(Request request, Converter<ResponseBody, T> responseConverter) throws IOException {
        if (httpCacheDirectoryProvider == null) {
            return null;
        }
        String requestMethod = request.method();
        if (requestMethod.equals("GET")) {
            String key = key(request.url());
            File file = new File(httpCacheDirectoryProvider.getDirectory(), key);
            String s = readFile(file);
            final MediaType MEDIA_TYPE = MediaType.get("application/json; charset=UTF-8");
            return Response.success(responseConverter.convert(ResponseBody.create(MEDIA_TYPE, s)));
        }
        return null;
    }

    private String readFile(File file) throws IOException {
        try (FileInputStream in = new FileInputStream(file)) {
            // size 为字串的长度 ，这里一次性读完
            int size = in.available();
            byte[] buffer = new byte[size];
            in.read(buffer);
            return new String(buffer);
        }
    }

    private void writeFile(File file, String content) throws IOException {
        try (FileOutputStream outStream = new FileOutputStream(file)) {
            outStream.write(content.getBytes());
        }
    }


    @Nullable
    public <T> void put(Response<T> response) throws IOException {
        if (httpCacheDirectoryProvider == null) {
            return;
        }
        String requestMethod = response.raw().request().method();
        if (requestMethod.equals("GET") && response.headers().get("Content-Type").toLowerCase().startsWith("application/json")) {
            Request request = response.raw().request();
            String key = key(request.url());
            File file = new File(httpCacheDirectoryProvider.getDirectory(), key);
            writeFile(file, new Gson().toJson(response.body()));
        }
    }

}
