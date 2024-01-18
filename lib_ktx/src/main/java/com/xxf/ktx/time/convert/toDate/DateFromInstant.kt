package com.xxf.ktx.time.convert.toDate

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.util.Date


@RequiresApi(Build.VERSION_CODES.O)
fun Instant.toDate(): Date {
    return Date.from(this)
}
