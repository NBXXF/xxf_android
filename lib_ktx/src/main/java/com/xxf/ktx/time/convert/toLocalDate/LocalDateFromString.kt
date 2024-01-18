package com.xxf.ktx.time.convert.toLocalDate

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
fun String.toLocalDate(pattern: String): LocalDate =
    LocalDate.parse(this, DateTimeFormatter.ofPattern(pattern))

