package com.xxf.utils

import android.text.TextUtils
import android.util.Log

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2018/9/3
 * Description ://日志扩展
 */
/**
 * 全局日志开关
 */
var isLoggable: Boolean = true

/**
 * 全局logTag
 */
var logTag: String = "====>XXF";


inline fun v(msg: String?, tr: Throwable? = null, tag: String = logTag): Int {
    if (!isLoggable) {
        return -1
    }
    getCurrentCodeLineNumberDesc()?.let {
        Log.v(tag, it)
    }
    return Log.v(tag, msg, tr)
}

inline fun d(msg: String?, tr: Throwable? = null, tag: String = logTag): Int {
    if (!isLoggable) {
        return -1
    }
    getCurrentCodeLineNumberDesc()?.let {
        Log.d(tag, it)
    }
    return Log.d(tag, msg, tr)
}


inline fun i(msg: String?, tr: Throwable? = null, tag: String = logTag): Int {
    if (!isLoggable) {
        return -1
    }
    getCurrentCodeLineNumberDesc()?.let {
        Log.i(tag, it)
    }
    return Log.i(tag, msg, tr)
}


inline fun w(msg: String?, tr: Throwable? = null, tag: String = logTag): Int {
    if (!isLoggable) {
        return -1
    }
    getCurrentCodeLineNumberDesc()?.let {
        Log.w(tag, it)
    }
    return Log.w(tag, msg, tr)
}


inline fun e(msg: String, tr: Throwable? = null, tag: String = logTag): Int {
    if (!isLoggable) {
        return -1
    }
    getCurrentCodeLineNumberDesc()?.let {
        Log.e(tag, it)
    }
    return Log.e(tag, msg, tr)
}

/**
 * 获取文件行号摘要信息
 */
inline fun getCurrentCodeLineNumberDesc(): String? {
    val trace: Array<StackTraceElement>? = Thread.currentThread().stackTrace;
    if (trace != null && trace.size > 3) {
        if (TextUtils.isEmpty(trace[3].fileName)) {
            return "[at] ${trace[3].className}"
        }
        return "[at] ${trace[3].fileName}(${trace[3].lineNumber})"
    }
    return null
}

