package com.xxf.ktx.time.convert.toLocalDate

import android.os.Build
import androidx.annotation.RequiresApi
import com.xxf.ktx.time.systemZoneId
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date


@RequiresApi(Build.VERSION_CODES.O)
fun Date.toLocalDate(zone: ZoneId = systemZoneId): LocalDate =
    this.toInstant().toLocalDate(zone)

