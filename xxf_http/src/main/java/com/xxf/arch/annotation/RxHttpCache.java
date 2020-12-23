package com.xxf.arch.annotation;

import java.lang.annotation.ElementType;

import retrofit2.CacheType;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.1
 * @Description 网络缓存
 * @date createTime：2018/9/7
 * <p>
 * 已经过时 请使用 @Cache CacheType cacheType
 */
@Deprecated
@java.lang.annotation.Target({ElementType.METHOD})
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@java.lang.annotation.Documented
@java.lang.annotation.Inherited
public @interface RxHttpCache {


    CacheType value() default CacheType.onlyRemote;
}
