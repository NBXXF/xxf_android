package com.xxf.ktx.time.convert

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.O)
fun Instant.toLocalDate(): LocalDate {
    return this.atZone(ZoneId.of("UTC")).toLocalDate()
}