package com.xxf.ktx

import android.os.SystemClock

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
 * @param block
 * @return 耗时 单位纳秒
 */
inline fun runCosting(block: () -> Unit): Long {
    val start = SystemClock.elapsedRealtimeNanos()
    block()
    val end = SystemClock.elapsedRealtimeNanos()
    return end - start
}
