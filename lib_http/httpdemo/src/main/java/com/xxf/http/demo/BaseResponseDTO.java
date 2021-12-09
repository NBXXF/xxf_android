package com.xxf.http.demo;

import com.xxf.arch.http.model.BaseHttpResult;

import retrofit2.CacheType;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2021/12/9
 * Description ://TODO
 */
public class BaseResponseDTO implements BaseHttpResult {
    CacheType cacheType;
    boolean isFromCache;
    int status;
    String message;

    @Override
    public void attachCacheConfig(CacheType cacheType, boolean isFromCache) {
        this.cacheType = cacheType;
        this.isFromCache = isFromCache;
    }

    @Override
    public String toString() {
        return "BaseResponseDTO{" +
                "cacheType=" + cacheType +
                ", isFromCache=" + isFromCache +
                ", status=" + status +
                ", message='" + message + '\'' +
                '}';
    }
}
