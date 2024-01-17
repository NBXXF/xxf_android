package com.xxf.ktx.time.format

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.format(pattern: String, locale: Locale? = null): String =
    dateTimeFormatterOf(pattern, locale).format(this)