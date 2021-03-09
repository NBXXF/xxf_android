package com.xxf.arch.annotation;

import com.xxf.arch.http.url.UrlProvider;

import java.lang.annotation.ElementType;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.1
 * @Description
 * @date createTime：2018/9/7
 */
@java.lang.annotation.Target({ElementType.TYPE})
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@java.lang.annotation.Documented
@java.lang.annotation.Inherited
public @interface BaseUrlProvider {

    /**
     * 绑定域名/主要路由,优先级高
     * @return
     */
    Class<? extends UrlProvider> value();
}