package com.xxf.ktx.time.convert.toLocalDateTime

import android.os.Build
import androidx.annotation.RequiresApi
import com.xxf.ktx.time.convert.toInstant.toInstant
import com.xxf.ktx.time.systemZoneId
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * 毫秒 转LocalDateTime
 */
@RequiresApi(Build.VERSION_CODES.O)
fun Long.toLocalDateTime(zone: ZoneId = systemZoneId): LocalDateTime =
    this.toInstant().toLocalDateTime(zone)