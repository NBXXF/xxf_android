package com.xxf.arch.test.http;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.xxf.arch.http.cache.HttpCacheConfigProvider;
import com.xxf.arch.test.BaseApplication;

import java.io.File;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description rxjava htpp默认缓存
 */
public class DefaultRxHttpCacheDirectoryProvider implements HttpCacheConfigProvider {

    @NonNull
    @Override
    public String getDirectory() {
        File file = new File(BaseApplication.getInstance().getCacheDir(), "okHttpCache4");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }


    @Override
    public boolean isCache(@Nullable Object body) {
        return true;
    }
}
