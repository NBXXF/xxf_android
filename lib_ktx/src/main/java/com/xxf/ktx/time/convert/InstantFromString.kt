package com.xxf.ktx.time.convert

import android.os.Build
import androidx.annotation.RequiresApi
import com.xxf.ktx.time.systemZoneId
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun String.toInstant(pattern: String, zone: ZoneId = systemZoneId): Instant =
    ZonedDateTime.parse(this, DateTimeFormatter.ofPattern(pattern).withZone(zone)).toInstant()

