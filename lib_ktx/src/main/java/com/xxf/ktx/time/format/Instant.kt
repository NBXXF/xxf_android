package com.xxf.ktx.time.format

import android.os.Build
import androidx.annotation.RequiresApi
import com.xxf.ktx.time.systemZoneId
import java.time.Instant
import java.time.ZoneId
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
fun Instant.format(pattern: String, zone: ZoneId = systemZoneId, locale: Locale? = null): String =
    dateTimeFormatterOf(pattern, locale).withZone(zone).format(this)
