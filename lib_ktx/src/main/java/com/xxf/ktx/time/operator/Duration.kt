package com.xxf.ktx.time.operator

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Duration


@RequiresApi(Build.VERSION_CODES.O)
operator fun Duration.unaryMinus(): Duration = this.negated()
@RequiresApi(Build.VERSION_CODES.O)
operator fun Duration.div(divisor: Long): Duration = this.dividedBy(divisor)
@RequiresApi(Build.VERSION_CODES.O)
operator fun Duration.times(multiplicand: Long): Duration = this.multipliedBy(multiplicand)


@RequiresApi(Build.VERSION_CODES.O)
infix fun Duration.min(that: Duration): Duration {
    return if (this < that) this else that
}

@RequiresApi(Build.VERSION_CODES.O)
infix fun Duration.max(that: Duration): Duration {
    return if (this > that) this else that
}

@RequiresApi(Build.VERSION_CODES.O)
fun minOf(a: Duration, b: Duration, vararg args: Duration): Duration {
    var min = if (a < b) a else b
    args.forEach {
        if (it < min)
            min = it
    }
    return min
}

@RequiresApi(Build.VERSION_CODES.O)
fun maxOf(a: Duration, b: Duration, vararg args: Duration): Duration {
    var max = if (a > b) a else b
    args.forEach {
        if (it > max)
            max = it
    }
    return max
}
