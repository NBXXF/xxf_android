package com.xxf.ktx.time.convert.toLocalTime

import android.os.Build
import androidx.annotation.RequiresApi
import com.xxf.ktx.time.convert.toLocalDateTime.toLocalDateTime
import com.xxf.ktx.time.systemZoneId
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.O)
fun Instant.toLocalTime(zone: ZoneId = systemZoneId): LocalTime =
    this.toLocalDateTime(zone)
        .toLocalTime()