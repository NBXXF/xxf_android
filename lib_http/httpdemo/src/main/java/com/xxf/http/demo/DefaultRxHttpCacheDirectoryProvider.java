package com.xxf.http.demo;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.xxf.application.ApplicationProviderKtKt;
;
import com.xxf.arch.http.cache.HttpCacheConfigProvider;

import java.io.File;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description rxjava htpp默认缓存
 */
public class DefaultRxHttpCacheDirectoryProvider implements HttpCacheConfigProvider {
    /**
     * 私有 仅限内部链接application
     * @return
     */
    private static Application getLinkedApplication(){
        return ApplicationProviderKtKt.getApplication();
    }

    @NonNull
    @Override
    public String getDirectory() {
        File file = new File(getLinkedApplication().getCacheDir(), "okHttpCache4");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }

}
