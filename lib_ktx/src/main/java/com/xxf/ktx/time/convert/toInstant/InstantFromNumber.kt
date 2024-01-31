package com.xxf.ktx.time.convert.toInstant

import android.os.Build
import androidx.annotation.RequiresApi
import com.xxf.ktx.time.convert.toLocalTime.toLocalTime
import java.time.Instant
import java.time.temporal.ChronoUnit

/**
 * 毫秒 转Instant
 */
@RequiresApi(Build.VERSION_CODES.O)
fun Long?.toInstant(unit: ChronoUnit = ChronoUnit.MILLIS): Instant =
    Instant.ofEpochMilli(unit.duration.multipliedBy(this?:0).toMillis())
