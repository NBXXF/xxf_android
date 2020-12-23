package com.xxf.arch.http.cache;

import androidx.annotation.NonNull;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description
 */
public interface HttpCacheDirectoryProvider {
    /**
     * 缓存目录 唯一标签 最好区分uid 或者token
     *
     * @return
     */
    @NonNull
    String getDirectory();

    /**
     * 最大缓存空间
     * @return
     */
    long maxSize();
}
