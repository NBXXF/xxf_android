package com.xxf.arch.http.cache;

import androidx.annotation.CheckResult;
import androidx.annotation.Nullable;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description
 */
public class RxHttpCacheFactory {

    /**
     * 缓存cache 实时获取cache路径 以方便应用层 切换用户与切换语言而导致缓存不同步问题
     */
    private static final Map<String, RxHttpCache> cacheMap = new ConcurrentHashMap<>();

    @CheckResult
    @Nullable
    public static synchronized RxHttpCache getCache(HttpCacheConfigProvider cacheDirectoryProvider) {
        RxHttpCache rxHttpCache = null;
        try {
            String dir = cacheDirectoryProvider.getDirectory();
            long size = cacheDirectoryProvider.maxSize();
            rxHttpCache = cacheMap.get(dir);
            if (rxHttpCache == null) {
                cacheMap.put(dir, rxHttpCache = new RxHttpCache(new File(dir), size));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rxHttpCache;
    }
}
