package com.xxf.arch.annotation;

import com.xxf.arch.http.converter.gson.GsonConvertInterceptor;

import java.lang.annotation.ElementType;

/**
 * @author xuanyouwu@163.com
 * @version 2.3.1
 * @Description
 * @date createTime：2018/9/7
 */
@java.lang.annotation.Target({ElementType.TYPE})
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@java.lang.annotation.Documented
@java.lang.annotation.Inherited
public @interface GsonInterceptor {
    /**
     * gson解析拦截器
     *
     * @return
     */
    Class<? extends GsonConvertInterceptor> value();
}