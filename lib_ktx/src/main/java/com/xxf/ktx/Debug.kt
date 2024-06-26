package com.xxf.ktx

import android.os.SystemClock
import kotlin.system.measureNanoTime
import kotlin.system.measureTimeMillis
import kotlin.time.measureTime
import kotlin.time.measureTimedValue

/**
 * 仅仅debug模式下才会执行
 * @param block
 * @return 是否是debug模式,也就是是否执行了
 */
inline fun runDebugging(block: () -> Unit): Boolean {
    if (application.isAppDebug) {
        block()
        return true
    }
    return false
}

/**
 * 计算耗时
 * 等价于kt自带 measureTime,measureNanoTime
 * 只不过刨除了cpu睡眠时间
 * @param block
 * @return 耗时 单位纳秒
 */
@Deprecated("纠正命名,请用runMeasureNanoTime", replaceWith = ReplaceWith("请用runMeasureNanoTime"))
inline fun runCosting(block: () -> Unit): Long {
    return runMeasureNanoTime(block)
}

/**
 * 计算耗时
 * 等价于kt自带 measureTime,measureNanoTime
 * 只不过刨除了cpu睡眠时间
 * @param block
 * @return 耗时 单位纳秒
 */
inline fun runMeasureNanoTime(block: () -> Unit): Long {
    val start = SystemClock.elapsedRealtimeNanos()
    block()
    val end = SystemClock.elapsedRealtimeNanos()
    return end - start
}

/**
 * 计算耗时
 * 等价于kt自带 measureTime,measureNanoTime
 * 只不过刨除了cpu睡眠时间
 * @param block
 * @return 耗时 单位纳秒
 */
inline fun runMeasureTimeMillis(block: () -> Unit): Long {
    val start = SystemClock.elapsedRealtime()
    block()
    return SystemClock.elapsedRealtime() - start
}