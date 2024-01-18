package com.xxf.ktx.time.convert.toDate

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.temporal.ChronoUnit
import java.util.Date

/**
 * 将数字转换成 默认单位MILLIS
 */
@RequiresApi(Build.VERSION_CODES.O)
fun Long.toDate(unit: ChronoUnit = ChronoUnit.MILLIS): Date {
    return Date(unit.duration.multipliedBy(this).toMillis())
}
