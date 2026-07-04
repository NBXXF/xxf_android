package com.xxf.http.demo;

import androidx.annotation.Nullable;


import com.nbxxf.kpower.http.model.BaseHttpResult;

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

    @Nullable
    @Override
    public CacheType getCacheType() {
        return null;
    }

    @Override
    public int getCode() {
        return 0;
    }

    @Override
    public void setCode(int i) {

    }

    @Nullable
    @Override
    public String getMsg() {
        return "";
    }

    @Override
    public void setMsg(@Nullable String s) {

    }

    @Nullable
    @Override
    public Object getData() {
        return null;
    }

    @Override
    public void setData(@Nullable Object o) {

    }

    @Override
    public void setCacheType(@Nullable CacheType cacheType) {

    }

    @Override
    public boolean isFromCache() {
        return false;
    }

    @Override
    public void setFromCache(boolean b) {

    }

    @Override
    public boolean isSuccess() {
        return false;
    }
}
