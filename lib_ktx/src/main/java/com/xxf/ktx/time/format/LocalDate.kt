package com.xxf.ktx.time.format

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.format(pattern: String, locale: Locale? = null): String =
    dateTimeFormatterOf(pattern, locale).format(this)

