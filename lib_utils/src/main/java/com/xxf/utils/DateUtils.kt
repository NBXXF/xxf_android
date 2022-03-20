package com.xxf.utils

import com.apache.commons.lang3.time.FastDateFormat
import java.util.*

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description 时间格式化
 */
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
}