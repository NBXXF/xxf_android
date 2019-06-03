
package com.xxf.arch.http.cache;


import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import io.reactivex.annotations.Nullable;
import okhttp3.Headers;
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
 * 可以继承 和公开 get方法
 * 目前只支持json
 */
@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class RxCache {
    public interface RxCachePrimaryKeyProvider {
        /**
         * 唯一标签,最好设置uid 或者token
         *
         * @return
         */
        @NonNull
        String getPrimaryKey(Request request);
    }

    private File directory;
    private RxCachePrimaryKeyProvider rxCachePrimaryKeyProvider;

    public RxCache(File directory, RxCachePrimaryKeyProvider rxCacheTagProvider) {
        this.directory = directory;
        this.rxCachePrimaryKeyProvider = rxCacheTagProvider;
    }

    public static String key(HttpUrl url) {
        return ByteString.encodeUtf8(url.toString()).md5().hex();
    }

    public static String key(String string) {
        return ByteString.encodeUtf8(string).md5().hex();
    }

    private File getDirectory() {
        if (!directory.exists()) {
            directory.mkdir();
        }
        return directory;
    }

    @Nullable
    public <T> Response<T> get(Request request, Converter<ResponseBody, T> responseConverter) throws IOException {
        String requestMethod = request.method();
        if (requestMethod.equals("GET")) {
            String key = key(rxCachePrimaryKeyProvider.getPrimaryKey(request));
            File file = new File(getDirectory(), key);
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
        String requestMethod = response.raw().request().method();
        if (requestMethod.equals("GET") && response.headers().get("Content-Type").toLowerCase().startsWith("application/json")) {
            Request request = response.raw().request();
            String key = key(rxCachePrimaryKeyProvider.getPrimaryKey(request));
            File file = new File(getDirectory(), key);
            writeFile(file, new Gson().toJson(response.body()));
        }
    }

}
