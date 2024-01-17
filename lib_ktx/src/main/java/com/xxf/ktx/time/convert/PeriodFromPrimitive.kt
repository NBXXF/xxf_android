package com.xxf.ktx.time.convert

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Period


/** Millsecond */
//fun Int.millis(): DurationBuilder = DurationBuilder(Period.millis(this))
@RequiresApi(Build.VERSION_CODES.O)
fun Int.millisPeriod(): Period = (this / 1000).secondsPeriod()

/** Seconds */
//fun Int.seconds(): DurationBuilder = DurationBuilder(Period.seconds(this))
@RequiresApi(Build.VERSION_CODES.O)
fun Int.secondsPeriod(): Period = (this / 60).minutesPeriod()

/** N Minutes */
//fun Int.minutes(): DurationBuilder = DurationBuilder(Period.minutes(this))
@RequiresApi(Build.VERSION_CODES.O)
fun Int.minutesPeriod(): Period = (this / 60).hoursPeriod()

/** N Hours */
//fun Int.hours(): DurationBuilder = DurationBuilder(Period.hours(this))
@RequiresApi(Build.VERSION_CODES.O)
fun Int.hoursPeriod(): Period = (this / 24).daysPeriod()

/** Period in N days */
@RequiresApi(Build.VERSION_CODES.O)
fun Int.daysPeriod(): Period = Period.ofDays(this)

/** Period in N weeks */
@RequiresApi(Build.VERSION_CODES.O)
fun Int.weeksPeriod(): Period = Period.ofWeeks(this)

/** Period in N months */
@RequiresApi(Build.VERSION_CODES.O)
fun Int.monthsPeriod(): Period = Period.ofMonths(this)

/** Period in N years */
@RequiresApi(Build.VERSION_CODES.O)
fun Int.yearsPeriod(): Period = Period.ofYears(this)
