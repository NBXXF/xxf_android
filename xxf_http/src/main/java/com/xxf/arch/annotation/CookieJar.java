package com.xxf.arch.annotation;

import com.xxf.arch.http.url.UrlProvider;

import java.lang.annotation.ElementType;

/**
 * @author xuanyouwu@163.com
 * @version 2.3.1
 * @Description cookie http
 * @date createTime：2018/9/7
 */
@java.lang.annotation.Target({ElementType.TYPE})
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@java.lang.annotation.Documented
@java.lang.annotation.Inherited
public @interface CookieJar {
    /**
     * 绑定cookie
     *
     * @return
     */
    Class<? extends okhttp3.CookieJar> value();
}