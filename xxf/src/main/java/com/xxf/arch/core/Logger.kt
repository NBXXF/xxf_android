package com.xxf.arch.core

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description
 */
interface Logger {
    val isLoggable: Boolean
    fun d(msg: String?)
    fun d(msg: String?, tr: Throwable?)
    fun e(msg: String?)
    fun e(msg: String?, tr: Throwable?)
}