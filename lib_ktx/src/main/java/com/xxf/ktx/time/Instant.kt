package com.xxf.ktx.time

import android.os.Build
import androidx.annotation.RequiresApi
import com.xxf.ktx.time.convert.toLocalDate
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Date


@RequiresApi(Build.VERSION_CODES.O)
fun Instant.resetSecondsToZero(): Instant {
    return this.truncatedTo(ChronoUnit.MINUTES)
}

@RequiresApi(Build.VERSION_CODES.O)
fun Instant.resetTimeToZero(): Instant {
    return this.truncatedTo(ChronoUnit.DAYS)
}

@RequiresApi(Build.VERSION_CODES.O)
fun Instant.endOfDay(): Instant {
    return this.resetTimeToZero()
        .plus(23, ChronoUnit.HOURS)
        .plus(59, ChronoUnit.MINUTES)
        .plusSeconds(59)
        .plusMillis(999)
}

@RequiresApi(Build.VERSION_CODES.O)
fun Instant.startOfDay(): Instant {
    return this.resetTimeToZero()
}

@RequiresApi(Build.VERSION_CODES.O)
fun Instant.toDate(): Date {
    return Date.from(this)
}

@RequiresApi(Build.VERSION_CODES.O)
fun Instant.dayOfWeek(): Int {
    return this.toLocalDate().dayOfWeek.value
}
