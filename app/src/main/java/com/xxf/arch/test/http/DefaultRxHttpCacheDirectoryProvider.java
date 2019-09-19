package com.xxf.arch.test.http;

import android.support.annotation.NonNull;

import com.xxf.arch.http.cache.HttpCacheDirectoryProvider;
import com.xxf.arch.test.BaseApplication;

import java.io.File;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description rxjava htpp默认缓存
 */
public class DefaultRxHttpCacheDirectoryProvider implements HttpCacheDirectoryProvider {

    @NonNull
    @Override
    public String getDirectory() {
        File file = new File(BaseApplication.getInstance().getCacheDir(), "okHttpCache");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }

    @Override
    public long maxSize() {
        //100M
        return 100 * 1024 * 1024;
    }
}
