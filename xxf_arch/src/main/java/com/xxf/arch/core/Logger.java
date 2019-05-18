package com.xxf.arch.core;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public interface Logger {

    boolean isLoggable();

    void d(String msg);

    void d(String msg, Throwable tr);

    void e(String msg);

    void e(String msg, Throwable tr);
}
