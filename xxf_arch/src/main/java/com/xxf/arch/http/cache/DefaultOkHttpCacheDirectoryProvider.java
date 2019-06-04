package com.xxf.arch.http.cache;

import android.support.annotation.NonNull;

import com.xxf.arch.XXF;

import java.io.File;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description okhttp 默认缓存
 */
public class DefaultOkHttpCacheDirectoryProvider implements HttpCacheDirectoryProvider {

    @NonNull
    @Override
    public String getDirectory() {
        File file = new File(XXF.getApplication().getCacheDir(), "rxHttpCache");
        if (!file.exists()) {
            file.mkdir();
        }
        return file.getAbsolutePath();
    }

    @Override
    public long maxSize() {
        //100M
        return 100 * 1024 * 1024;
    }
}
