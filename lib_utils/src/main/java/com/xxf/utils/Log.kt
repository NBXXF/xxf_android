package com.xxf.utils

import android.util.Log

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2018/9/3
 * Description ://日志扩展
 */
/**
 * 全局日志开关
 */
var isLoggable: Boolean = false

/**
 * 全局logTag
 */
var logTag: String = "====>XXF";


inline fun v(msg: String?, tr: Throwable? = null, tag: String = logTag): Int {
    if (!isLoggable) {
        return -1
    }
    return Log.v(tag, msg, tr)
}

inline fun d(msg: String?, tr: Throwable? = null, tag: String = logTag): Int {
    if (!isLoggable) {
        return -1
    }
    return Log.d(tag, msg, tr)
}


inline fun i(msg: String?, tr: Throwable? = null, tag: String = logTag): Int {
    if (!isLoggable) {
        return -1
    }
    return Log.i(tag, msg, tr)
}


inline fun w(msg: String?, tr: Throwable? = null, tag: String = logTag): Int {
    if (!isLoggable) {
        return -1
    }
    return Log.w(tag, msg, tr)
}


inline fun e(msg: String, tr: Throwable? = null, tag: String = logTag): Int {
    if (!isLoggable) {
        return -1
    }
    return Log.e(tag, msg, tr)
}