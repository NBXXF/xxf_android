package com.xxf.utils

import android.text.Spannable

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2021/12/7
 * Description ://span工具类
 */
fun <T> Spannable.clearSpans(start: Int, end: Int, type: Class<T>) {
    getSpans(
        start,
        end,
        type
    )?.forEach {
        removeSpan(it)
    }
}

object SpanUtils {
}