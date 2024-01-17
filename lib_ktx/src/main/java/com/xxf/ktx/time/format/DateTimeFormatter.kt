package com.xxf.ktx.time.format

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
fun dateTimeFormatterOf(pattern: String, locale: Locale?): DateTimeFormatter =
    if (locale != null) {
        DateTimeFormatter.ofPattern(pattern, locale)
    } else {
        DateTimeFormatter.ofPattern(pattern)
    }
