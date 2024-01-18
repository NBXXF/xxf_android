package com.xxf.ktx.time

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalTime
import java.time.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.O)
fun LocalTime.startOfDay(): LocalTime {
    return LocalTime.MIN
}

@RequiresApi(Build.VERSION_CODES.O)
fun LocalTime.endOfDay(): LocalTime {
    return LocalTime.MAX
}

@RequiresApi(Build.VERSION_CODES.O)
fun LocalTime.startOfHour(): LocalTime {
    return this.truncatedTo(ChronoUnit.HOURS)
}

@RequiresApi(Build.VERSION_CODES.O)
fun LocalTime.endOfHour(): LocalTime {
    return startOfHour()
        .plus(1, ChronoUnit.HOURS)
        .plus(-1, ChronoUnit.NANOS)
}

@RequiresApi(Build.VERSION_CODES.O)
fun LocalTime.startOfMinutes(): LocalTime {
    return this.truncatedTo(ChronoUnit.MINUTES)
}

@RequiresApi(Build.VERSION_CODES.O)
fun LocalTime.endOfMinutes(): LocalTime {
    return startOfMinutes()
        .plus(1, ChronoUnit.MINUTES)
        .plus(-1, ChronoUnit.NANOS)
}

@RequiresApi(Build.VERSION_CODES.O)
fun LocalTime.startOfSeconds(): LocalTime {
    return this.truncatedTo(ChronoUnit.SECONDS)
}

@RequiresApi(Build.VERSION_CODES.O)
fun LocalTime.endOfSeconds(): LocalTime {
    return startOfSeconds()
        .plus(1, ChronoUnit.SECONDS)
        .plus(-1, ChronoUnit.NANOS)
}
