package com.xxf.application.clicks

import android.graphics.PointF
import android.os.SystemClock
import android.util.Range
import android.util.SizeF

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.1
 * @Description  连续点击事件捕捉 类似打开android 开发者模式
 * @date createTime：2018/9/7
 */
open class QuickClicksHandler(
    val count: Int = 5, // 点击次数
    val duration: Long = 1000 // 规定有效时间 毫秒
) {
    private val ignorePoint = PointF(-1.0f, -1.0f);
    private var mHits: LongArray = LongArray(count)
    private var mHitsLocations: Array<PointF> = Array<PointF>(count) { ignorePoint }

    /**
     * @param eventLocation 事件发生位置
     * @param allowOffset 允许的偏差像素
     * @param result 响应条件满足回调
     */
    fun handle(
        eventLocation: PointF = ignorePoint,
        allowOffset: SizeF = SizeF(10.0f, 10.0f),
        result: () -> Unit
    ) {
        //每次点击时，数组向前移动一位
        System.arraycopy(mHits, 1, mHits, 0, mHits.size - 1)
        System.arraycopy(mHitsLocations, 1, mHitsLocations, 0, mHitsLocations.size - 1)

        //为数组最后一位赋值
        mHits[mHits.size - 1] = SystemClock.uptimeMillis()
        mHitsLocations[mHitsLocations.size - 1] = eventLocation
        if (mHits[0] >= SystemClock.uptimeMillis() - duration && isInRange(allowOffset)) {
            mHits = LongArray(count) //重新初始化数组
            mHitsLocations = Array<PointF>(count) { ignorePoint }
            result()
        }
    }

    /**
     * 是否在这个允许的区间
     */
    private fun isInRange(allowOffset: SizeF): Boolean {
        val rangeOfX = Range(mHitsLocations.minOf { it.x }, mHitsLocations.maxOf { it.x })
        val rangeOfY = Range(mHitsLocations.minOf { it.y }, mHitsLocations.maxOf { it.y })
        return rangeOfX.run {
            (upper - lower) <= allowOffset.width
        } &&
                rangeOfY.run {
                    (upper - lower) <= allowOffset.height
                }
    }

}