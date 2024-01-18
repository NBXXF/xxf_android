package com.xxf.ktx.time.convert.toLocalDate

import android.os.Build
import androidx.annotation.RequiresApi
import com.xxf.ktx.time.systemZoneId
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.O)
fun Instant.toLocalDate(zone:ZoneId= systemZoneId): LocalDate {
    return this.atZone(zone).toLocalDate()
}