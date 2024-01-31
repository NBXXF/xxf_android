package com.xxf.ktx.time.convert.toDuration

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Duration
import java.time.temporal.ChronoUnit


@RequiresApi(Build.VERSION_CODES.O)
fun Long?.toDuration(unit: ChronoUnit = ChronoUnit.MILLIS): Duration =
    unit.duration.multipliedBy(this?:0)
