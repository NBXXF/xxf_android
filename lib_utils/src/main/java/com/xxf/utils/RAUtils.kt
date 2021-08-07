package com.xxf.utils

import android.view.MotionEvent
import android.view.View
import java.util.concurrent.ConcurrentHashMap

/**
 * @author xuanyouwu
 * @email xuanyouwu@163.com
 * @time 2016-11-10 17:50
 * 防爆点击
 */
object RAUtils {
    /**
     * 记录一个路由上次跳转的时间
     */
    private val routerJumpRecordLastTimes: MutableMap<String, Long> = ConcurrentHashMap()
    const val DURATION_DEFAULT: Long = 500
    val isLegalDefault: Boolean
        get() = isLegal(DURATION_DEFAULT)

    /**
     * 防爆 阻力 false 表示暴力点击
     *
     * @param duration 点击间隔
     * @return
     */
    fun isLegal(duration: Long): Boolean {
        return isLegal(RAUtils::class.java.name, duration)
    }

    fun isLegal(path: String, duration: Long): Boolean {
        val lastRecord = routerJumpRecordLastTimes[path]
        val current = System.currentTimeMillis()
        routerJumpRecordLastTimes[path] = current
        return lastRecord == null || current - lastRecord >= duration
    }

    fun inRangeOfView(view: View, ev: MotionEvent): Boolean {
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        val x = location[0]
        val y = location[1]
        return if (ev.x < x || ev.x > x + view.width || ev.y < y || ev.y > y + view.height) {
            false
        } else true
    }
}