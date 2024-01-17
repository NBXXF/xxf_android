package com.xxf.ktx.time.operator

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Duration
import java.time.LocalDateTime
import java.time.Period

@RequiresApi(Build.VERSION_CODES.O)
/** `-` operator for `LocalDateTime` */
operator fun LocalDateTime.minus(period: Period): LocalDateTime = this.minus(period)

/** `-` operator for `LocalDateTime` */
@RequiresApi(Build.VERSION_CODES.O)
operator fun LocalDateTime.minus(duration: Duration): LocalDateTime = this.minus(duration)

/** `+` operator for `LocalDateTime` */
@RequiresApi(Build.VERSION_CODES.O)
operator fun LocalDateTime.plus(period: Period): LocalDateTime = this.plus(period)

/** `+` operator for `LocalDateTime` */
@RequiresApi(Build.VERSION_CODES.O)
operator fun LocalDateTime.plus(duration: Duration): LocalDateTime = this.plus(duration)



/** get minimum [LocalDateTime] */
@RequiresApi(Build.VERSION_CODES.O)
infix fun LocalDateTime.min(that: LocalDateTime): LocalDateTime {
    return if (this < that) this else that
}

/** get maximum [LocalDateTime] */
@RequiresApi(Build.VERSION_CODES.O)
infix fun LocalDateTime.max(that: LocalDateTime): LocalDateTime {
    return if (this > that) this else that
}

@RequiresApi(Build.VERSION_CODES.O)
fun minOf(a: LocalDateTime, b: LocalDateTime, vararg args: LocalDateTime): LocalDateTime {
    var min = if (a < b) a else b
    args.forEach {
        if (it < min) {
            min = it
        }
    }
    return min
}

@RequiresApi(Build.VERSION_CODES.O)
fun maxOf(a: LocalDateTime, b: LocalDateTime, vararg args: LocalDateTime): LocalDateTime {
    var max = if (a < b) b else a
    args.forEach {
        if (it > max) {
            max = it
        }
    }
    return max
}
