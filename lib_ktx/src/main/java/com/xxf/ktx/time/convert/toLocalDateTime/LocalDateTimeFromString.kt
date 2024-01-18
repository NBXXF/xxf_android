package com.xxf.ktx.time.convert.toLocalDateTime

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun String.toLocalDateTime(pattern: String): LocalDateTime =
    LocalDateTime.parse(this, DateTimeFormatter.ofPattern(pattern))

@RequiresApi(Build.VERSION_CODES.O)
fun String.toLocalDateTime(formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME): LocalDateTime =
    LocalDateTime.parse(this, formatter)