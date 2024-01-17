package com.xxf.ktx.time.operator

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Period

@RequiresApi(Build.VERSION_CODES.O)
operator fun Period.minus(period: Period?): Period = this.minus(period)
@RequiresApi(Build.VERSION_CODES.O)
operator fun Period.plus(period: Period?): Period = this.plus(period)

@RequiresApi(Build.VERSION_CODES.O)
operator fun Period.times(scalar: Int): Period = this.multipliedBy(scalar)
@RequiresApi(Build.VERSION_CODES.O)
operator fun Period.unaryMinus(): Period = this.negated()


@RequiresApi(Build.VERSION_CODES.O)
operator fun Long.times(period: Period): Period = period.multipliedBy(this.toInt())