package com.xxf.ktx.time.operator

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.Period


/** `-` operator for `LocalDate` */
@RequiresApi(Build.VERSION_CODES.O)
operator fun LocalDate.minus(period: Period): LocalDate = this.minus(period)

/** `-` operator for `LocalDate` */
@RequiresApi(Build.VERSION_CODES.O)
operator fun LocalDate.minus(duration: Duration): LocalDate = this.minus(duration)

/** `+` operator for `LocalDate` */
@RequiresApi(Build.VERSION_CODES.O)
operator fun LocalDate.plus(period: Period): LocalDate = this.plus(period)

/** `+` operator for `LocalDate` */
@RequiresApi(Build.VERSION_CODES.O)
operator fun LocalDate.plus(duration: Duration): LocalDate = this.plus(duration)


/** get minimum [LocalTime] */
@RequiresApi(Build.VERSION_CODES.O)
infix fun LocalDate.min(that: LocalDate): LocalDate {
    return if (this < that) this else that
}

/** get maximum [LocalTime] */
@RequiresApi(Build.VERSION_CODES.O)
infix fun LocalDate.max(that: LocalDate): LocalDate {
    return if (this > that) this else that
}

@RequiresApi(Build.VERSION_CODES.O)
fun minOf(a: LocalDate, b: LocalDate, vararg args: LocalDate): LocalDate {
    var min = if (a < b) a else b
    args.forEach {
        if (it < min) {
            min = it
        }
    }
    return min
}

@RequiresApi(Build.VERSION_CODES.O)
fun maxOf(a: LocalDate, b: LocalDate, vararg args: LocalDate): LocalDate {
    var max = if (a < b) b else a
    args.forEach {
        if (it > max) {
            max = it
        }
    }
    return max
}

