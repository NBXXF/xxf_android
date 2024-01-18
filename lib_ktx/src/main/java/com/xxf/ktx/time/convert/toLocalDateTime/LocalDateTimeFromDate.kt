package com.xxf.ktx.time.convert.toLocalDateTime

import android.os.Build
import androidx.annotation.RequiresApi
import com.xxf.ktx.time.systemZoneId
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

@RequiresApi(Build.VERSION_CODES.O)
fun Date.toLocalDateTime(zone: ZoneId = systemZoneId): LocalDateTime =
    this.toInstant().atZone(zone).toLocalDateTime()