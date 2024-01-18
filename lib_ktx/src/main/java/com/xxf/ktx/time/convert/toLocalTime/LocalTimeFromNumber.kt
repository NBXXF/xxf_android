package com.xxf.ktx.time.convert.toLocalTime

import android.os.Build
import androidx.annotation.RequiresApi
import com.xxf.ktx.time.convert.toLocalDateTime.toLocalDateTime
import com.xxf.ktx.time.systemZoneId
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit

/**
 * 将数字转换成 默认单位MILLIS
 */
@RequiresApi(Build.VERSION_CODES.O)
fun Long.toLocalTime(
    unit: ChronoUnit = ChronoUnit.MILLIS, zone: ZoneId = systemZoneId
): LocalTime =
    Instant.ofEpochMilli(unit.duration.multipliedBy(this).toMillis())
        .toLocalDateTime(zone)
        .toLocalTime()