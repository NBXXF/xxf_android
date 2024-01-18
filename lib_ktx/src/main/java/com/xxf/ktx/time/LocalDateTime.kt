package com.xxf.ktx.time

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.toEpochSecond(zone: ZoneId = systemZoneId): Long =
    atZone(zone).toEpochSecond()

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.toEpochMilli(zone: ZoneId = systemZoneId): Long =
    toEpochSecond(zone) * 1000 + toLocalTime().nano / 1000000


@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.isToday(zone: ZoneId = systemZoneId): Boolean = toLocalDate().isToday(zone)

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.isToday(zone: ZoneId = systemZoneId): Boolean = this == LocalDate.now(zone)

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.isYesterday(zone: ZoneId = systemZoneId): Boolean =
    toLocalDate().isYesterday(zone)

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.isYesterday(zone: ZoneId = systemZoneId): Boolean =
    this == LocalDate.now(zone).minus(1, ChronoUnit.DAYS)

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.startOfDay(): LocalDateTime = LocalDateTime.of(this.toLocalDate(), LocalTime.MIN)

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.endOfDay(): LocalDateTime = LocalDateTime.of(this.toLocalDate(), LocalTime.MAX)

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.firstDayOfYear(): LocalDateTime = with(TemporalAdjusters.firstDayOfYear())

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.lastDayOfYear(): LocalDateTime = with(TemporalAdjusters.lastDayOfYear())

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.firstDayOfNextYear(): LocalDateTime = with(TemporalAdjusters.firstDayOfNextYear())

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.firstDayOfLastYear(): LocalDateTime =
    with { it.with(ChronoField.DAY_OF_YEAR, 1).minus(1, ChronoUnit.YEARS) }

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.firstDayOfMonth(): LocalDateTime = with(TemporalAdjusters.firstDayOfMonth())

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.lastDayOfMonth(): LocalDateTime = with(TemporalAdjusters.lastDayOfMonth())

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.firstDayOfNextMonth(): LocalDateTime =
    with(TemporalAdjusters.firstDayOfNextMonth())

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.firstDayOfLastMonth(): LocalDateTime =
    with { it.with(ChronoField.DAY_OF_MONTH, 1).minus(1, ChronoUnit.MONTHS) }

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.firstInMonth(dayOfWeek: DayOfWeek): LocalDateTime =
    with(TemporalAdjusters.firstInMonth(dayOfWeek))

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.lastInMonth(dayOfWeek: DayOfWeek): LocalDateTime =
    with(TemporalAdjusters.lastInMonth(dayOfWeek))

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.dayOfWeekInMonth(ordinal: Int, dayOfWeek: DayOfWeek): LocalDateTime =
    with(TemporalAdjusters.dayOfWeekInMonth(ordinal, dayOfWeek))

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.next(dayOfWeek: DayOfWeek): LocalDateTime =
    with(TemporalAdjusters.next(dayOfWeek))

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.nextOrSame(dayOfWeek: DayOfWeek): LocalDateTime =
    with(TemporalAdjusters.nextOrSame(dayOfWeek))

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.previous(dayOfWeek: DayOfWeek): LocalDateTime =
    with(TemporalAdjusters.previous(dayOfWeek))

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.previousOrSame(dayOfWeek: DayOfWeek): LocalDateTime =
    with(TemporalAdjusters.previousOrSame(dayOfWeek))



