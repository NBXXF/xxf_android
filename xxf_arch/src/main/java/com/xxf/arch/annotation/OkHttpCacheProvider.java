package com.xxf.arch.annotation;

import com.xxf.arch.http.cache.DefaultOkHttpCacheDirectoryProvider;
import com.xxf.arch.http.cache.HttpCacheDirectoryProvider;

import java.lang.annotation.ElementType;

/**
 * @author xuanyouwu@163.com
 * @version 2.3.1
 * @Description okhttp网络缓存 文件
 * @date createTime：2018/9/7
 */
@java.lang.annotation.Target({ElementType.TYPE})
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@java.lang.annotation.Documented
@java.lang.annotation.Inherited
public @interface OkHttpCacheProvider {

    Class<? extends HttpCacheDirectoryProvider> value() default DefaultOkHttpCacheDirectoryProvider.class;
}
