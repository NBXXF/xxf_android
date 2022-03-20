package com.xxf.utils

import com.apache.commons.lang3.time.FastDateFormat
import java.util.*

object DateUtils {
    fun format(millis: Long, pattern: String): String? {
        return try {
            FastDateFormat.getInstance(pattern).format(millis)
        } catch (e: Throwable) {
            null
        }
    }

    fun format(date: Date, pattern: String): String? {
        return try {
            FastDateFormat.getInstance(pattern).format(date)
        } catch (e: Throwable) {
            null
        }
    }
}