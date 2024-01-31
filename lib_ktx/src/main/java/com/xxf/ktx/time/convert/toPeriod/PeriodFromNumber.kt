package com.xxf.ktx.time.convert.toPeriod

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Period
import java.time.temporal.ChronoUnit


@RequiresApi(Build.VERSION_CODES.O)
fun Long?.toPeriod(unit: ChronoUnit = ChronoUnit.MILLIS): Period =
    Period.from(unit.duration.multipliedBy(this ?: 0))

