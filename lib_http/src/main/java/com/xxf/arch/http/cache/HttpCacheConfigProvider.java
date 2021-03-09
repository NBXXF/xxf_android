package com.xxf.arch.http.cache;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.concurrent.TimeUnit;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description
 */
public interface HttpCacheConfigProvider {
    /**
     * 缓存目录 唯一标签 最好区分uid 或者token
     *
     * @return
     */
    @NonNull
    String getDirectory();

    /**
     * 最大缓存空间
     * 单位File.length() B
     *
     * @return
     */
    default long maxSize() {
        return 1024 * 1024 * 100;
    }

    /**
     * 是否缓存 场用于 用于下游判断code是否应该缓存
     *
     * @param body 在interface 中声明的 Observable<T> 对应的T
     * @return
     */
    default boolean isCache(@Nullable Object body) {
        return true;
    }

    /**
     * 全局默认缓存时间 优先级低于->参数传递的@Header("cache") 或者在方法头上加的@Header("cache:xxx")
     * 注意单位是毫秒
     *
     * @return
     * @GET("xxxxx") Observable<JsonObject> getCity3(@Header("cache") long time, @Cache CacheType cacheType);
     */
    default long cacheTime() {
        return TimeUnit.DAYS.toMillis(1);
    }
}
