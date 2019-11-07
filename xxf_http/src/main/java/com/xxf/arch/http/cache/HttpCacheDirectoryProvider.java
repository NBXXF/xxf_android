package com.xxf.arch.http.cache;

import android.support.annotation.NonNull;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
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
