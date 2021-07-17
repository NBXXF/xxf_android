package com.xxf.arch.annotation;

import java.lang.annotation.ElementType;

/**
 * @version 2.3.1
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description 设置调度最大并发数量
 * @date createTime：2018/9/7
 */
@java.lang.annotation.Target({ElementType.TYPE})
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@java.lang.annotation.Documented
@java.lang.annotation.Inherited
public @interface Dispatcher {
    /**
     * 最大并发数量
     * okhttp 默认:64
     *
     * @return
     */
    int maxRequests();

    /**
     * 每个主机最大请求数
     * okhttp 默认:5
     *
     * @return
     */
    int maxRequestsPerHost();
}
