package com.xxf.ktx.time.operator

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Duration
import java.time.LocalTime
import java.time.Period


/** `-` operator for `LocalTime` */
@RequiresApi(Build.VERSION_CODES.O)
operator fun LocalTime.minus(period: Period): LocalTime = this.minus(period)

/** `-` operator for `LocalTime` */
@RequiresApi(Build.VERSION_CODES.O)
operator fun LocalTime.minus(duration: Duration): LocalTime = this.minus(duration)

/** `+` operator for `LocalTime` */
@RequiresApi(Build.VERSION_CODES.O)
operator fun LocalTime.plus(period: Period): LocalTime = this.plus(period)

/** `+` operator for `LocalTime` */
@RequiresApi(Build.VERSION_CODES.O)
operator fun LocalTime.plus(duration: Duration): LocalTime = this.plus(duration)


/** get minimum [LocalTime] */
@RequiresApi(Build.VERSION_CODES.O)
infix fun LocalTime.min(that: LocalTime): LocalTime {
    return if (this < that) this else that
}

/** get maximum [LocalTime] */
@RequiresApi(Build.VERSION_CODES.O)
infix fun LocalTime.max(that: LocalTime): LocalTime {
    return if (this > that) this else that
}

@RequiresApi(Build.VERSION_CODES.O)
fun minOf(a: LocalTime, b: LocalTime, vararg args: LocalTime): LocalTime {
    var min = if (a < b) a else b
    args.forEach {
        if (it < min) {
            min = it
        }
    }
    return min
}

@RequiresApi(Build.VERSION_CODES.O)
fun maxOf(a: LocalTime, b: LocalTime, vararg args: LocalTime): LocalTime {
    var max = if (a < b) b else a
    args.forEach {
        if (it > max) {
            max = it
        }
    }
    return max
}
