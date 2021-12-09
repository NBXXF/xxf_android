package com.xxf.arch.http.model;

import retrofit2.CacheType;

/**
 * @version 2.3.1
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description
 * @date createTime：2018/9/7
 */
public interface BaseHttpResult {

    /**
     * 同步缓存参数 以告知业务
     *
     * @param cacheType
     * @param isFromCache 是否来自缓存数据
     */
    void attachCacheConfig(CacheType cacheType, boolean isFromCache);
}
