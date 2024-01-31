@file:Suppress("unused")

package com.xxf.ktx.time

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.*
import java.time.temporal.ChronoField.DAY_OF_MONTH
import java.time.temporal.ChronoField.DAY_OF_YEAR
import java.time.temporal.ChronoUnit.MONTHS
import java.time.temporal.ChronoUnit.YEARS
import java.time.temporal.TemporalAdjusters
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.startOfDay(): LocalDate = LocalDateTime.of(this, LocalTime.MIN).toLocalDate()

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.endOfDay(): LocalDate = LocalDateTime.of(this, LocalTime.MAX).toLocalDate()

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.firstDayOfYear(): LocalDate = with(TemporalAdjusters.firstDayOfYear())

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.lastDayOfYear(): LocalDate = with(TemporalAdjusters.lastDayOfYear())

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.firstDayOfNextYear(): LocalDate = with(TemporalAdjusters.firstDayOfNextYear())

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.firstDayOfLastYear(): LocalDate = with { it.with(DAY_OF_YEAR, 1).minus(1, YEARS) }

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.firstDayOfMonth(): LocalDate = with(TemporalAdjusters.firstDayOfMonth())

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.lastDayOfMonth(): LocalDate = with(TemporalAdjusters.lastDayOfMonth())

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.firstDayOfNextMonth(): LocalDate = with(TemporalAdjusters.firstDayOfNextMonth())

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.firstDayOfLastMonth(): LocalDate = with { it.with(DAY_OF_MONTH, 1).minus(1, MONTHS) }

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.firstInMonth(dayOfWeek: DayOfWeek): LocalDate =
    with(TemporalAdjusters.firstInMonth(dayOfWeek))

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.lastInMonth(dayOfWeek: DayOfWeek): LocalDate =
    with(TemporalAdjusters.lastInMonth(dayOfWeek))

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.dayOfWeekInMonth(ordinal: Int, dayOfWeek: DayOfWeek): LocalDate =
    with(TemporalAdjusters.dayOfWeekInMonth(ordinal, dayOfWeek))

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.next(dayOfWeek: DayOfWeek): LocalDate = with(TemporalAdjusters.next(dayOfWeek))

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.nextOrSame(dayOfWeek: DayOfWeek): LocalDate =
    with(TemporalAdjusters.nextOrSame(dayOfWeek))

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.previous(dayOfWeek: DayOfWeek): LocalDate =
    with(TemporalAdjusters.previous(dayOfWeek))

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.previousOrSame(dayOfWeek: DayOfWeek): LocalDate =
    with(TemporalAdjusters.previousOrSame(dayOfWeek))

