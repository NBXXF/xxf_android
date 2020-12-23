package com.xxf.arch.core;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description
 */
public interface Logger {

    boolean isLoggable();

    void d(String msg);

    void d(String msg, Throwable tr);

    void e(String msg);

    void e(String msg, Throwable tr);
}
