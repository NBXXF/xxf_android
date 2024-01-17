package com.xxf.ktx.time.convert

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Duration


@RequiresApi(Build.VERSION_CODES.O)
fun Int.dayDuration(): Duration = Duration.ofDays(this.toLong())

@RequiresApi(Build.VERSION_CODES.O)
fun Int.hourDuration(): Duration = Duration.ofHours(this.toLong())

@RequiresApi(Build.VERSION_CODES.O)
fun Int.minuteDuration(): Duration = Duration.ofMinutes(this.toLong())

@RequiresApi(Build.VERSION_CODES.O)
fun Int.secondDuration(): Duration = Duration.ofSeconds(this.toLong())

@RequiresApi(Build.VERSION_CODES.O)
fun Int.milliDuration(): Duration = Duration.ofMillis(this.toLong())

@RequiresApi(Build.VERSION_CODES.O)
fun Long.dayDuration(): Duration = Duration.ofDays(this)

@RequiresApi(Build.VERSION_CODES.O)
fun Long.hourDuration(): Duration = Duration.ofHours(this)

@RequiresApi(Build.VERSION_CODES.O)
fun Long.minuteDuration(): Duration = Duration.ofMinutes(this)

@RequiresApi(Build.VERSION_CODES.O)
fun Long.secondDuration(): Duration = Duration.ofSeconds(this)

@RequiresApi(Build.VERSION_CODES.O)
fun Long.milliDuration(): Duration = Duration.ofMillis(this)

