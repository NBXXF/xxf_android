package com.xxf.utils

import com.apache.commons.lang3.time.FastDateFormat
import java.util.Date

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description 时间格式化
 */
fun Long?.formatTimeString(pattern: String = "yyyy-MM-dd HH:mm:ss"): String? {
    return DateUtils.format(pattern, this ?: 0)
}

fun Date?.formatTimeString(pattern: String = "yyyy-MM-dd HH:mm:ss"): String? {
    return DateUtils.format(pattern, this ?: Date())
}

fun String?.parseTimeString(pattern: String = "yyyy-MM-dd HH:mm:ss"): Date? {
    return DateUtils.parse(pattern, this ?: "0")
}

object DateUtils {
    fun format(pattern: String, millis: Long): String? {
        return try {
            FastDateFormat.getInstance(pattern).format(millis)
        } catch (e: Throwable) {
            null
        }
    }

    fun format(pattern: String, date: Date): String? {
        return try {
            FastDateFormat.getInstance(pattern).format(date)
        } catch (e: Throwable) {
            null
        }
    }

    fun parse(pattern: String, source: String): Date? {
        return try {
            FastDateFormat.getInstance(pattern).parse(source)
        } catch (e: Throwable) {
            null
        }
    }
}