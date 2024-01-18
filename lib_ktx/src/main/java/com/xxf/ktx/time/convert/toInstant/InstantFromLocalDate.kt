package com.xxf.ktx.time.convert.toInstant

import android.os.Build
import androidx.annotation.RequiresApi
import com.xxf.ktx.time.systemZoneId
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId


@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.toInstant(zone: ZoneId = systemZoneId): Instant = this.atStartOfDay().toInstant(zone);
