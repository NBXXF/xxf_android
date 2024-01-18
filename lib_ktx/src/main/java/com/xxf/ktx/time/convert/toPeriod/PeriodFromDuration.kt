package com.xxf.ktx.time.convert.toPeriod

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Duration
import java.time.Period


@RequiresApi(Build.VERSION_CODES.O)
fun Duration.toPeriod(): Period = Period.from(this)

