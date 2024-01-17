package com.xxf.ktx.time.operator

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Duration
import java.time.Instant
import java.time.Period

@RequiresApi(Build.VERSION_CODES.O)
operator fun Instant.minus(millis: Long): Instant = this.minusMillis(millis)

@RequiresApi(Build.VERSION_CODES.O)
operator fun Instant.minus(duration: Duration): Instant = this.minus(duration)

@RequiresApi(Build.VERSION_CODES.O)
operator fun Instant.minus(period: Period): Instant = this.minus(period)

@RequiresApi(Build.VERSION_CODES.O)
operator fun Instant.plus(millis: Long): Instant = this.plusMillis(millis)

@RequiresApi(Build.VERSION_CODES.O)
operator fun Instant.plus(duration: Duration): Instant = this.plus(duration)

@RequiresApi(Build.VERSION_CODES.O)
operator fun Instant.plus(period: Period): Instant = this.plus(period)


@RequiresApi(Build.VERSION_CODES.O)
fun <T : Instant> minOf(a: T, b: T, vararg args: T): T {
    var min = if (a < b) a else b
    args.forEach {
        if (it < min)
            min = it
    }
    return min
}

@RequiresApi(Build.VERSION_CODES.O)
fun <T : Instant> maxOf(a: T, b: T, vararg args: T): T {
    var max = if (a > b) a else b
    args.forEach {
        if (it > max)
            max = it
    }
    return max
}

@RequiresApi(Build.VERSION_CODES.O)
infix fun <T : Instant> T.min(that: T): T {
    return if (this < that) this else that
}

@RequiresApi(Build.VERSION_CODES.O)
infix fun <T : Instant> T.max(that: T): T {
    return if (this > that) this else that
}

