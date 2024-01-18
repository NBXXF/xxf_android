package com.xxf.ktx.time.convert.toInstant

import android.os.Build
import androidx.annotation.RequiresApi
import com.xxf.ktx.time.systemZoneId
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId


@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.toInstant(zone: ZoneId = systemZoneId): Instant =
    atZone(zone).toInstant()
