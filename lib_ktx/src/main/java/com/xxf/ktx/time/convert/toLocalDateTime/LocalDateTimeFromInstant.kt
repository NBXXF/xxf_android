package com.xxf.ktx.time.convert.toLocalDateTime

import android.os.Build
import androidx.annotation.RequiresApi
import com.xxf.ktx.time.systemZoneId
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.O)
fun Instant.toLocalDateTime(zone: ZoneId = systemZoneId): LocalDateTime =
    LocalDateTime.ofInstant(this, zone)