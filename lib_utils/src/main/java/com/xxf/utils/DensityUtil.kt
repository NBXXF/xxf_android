package com.xxf.utils

import android.util.TypedValue
import com.xxf.application.applicationContext

/**
 * Description 单位换算工具
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：16/6/23
 * version
 */
val Float.dp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        applicationContext.resources.displayMetrics
    )

val Float.sp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this,
        applicationContext.resources.displayMetrics
    )

val Int.dp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        applicationContext.resources.displayMetrics
    ).toInt()

val Int.sp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this.toFloat(),
        applicationContext.resources.displayMetrics
    ).toInt()

object DensityUtil {

    @JvmStatic
    fun dip2px(dpValue: Float): Int {
        val scale =applicationContext.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    @JvmStatic
    fun px2dip(pxValue: Float): Int {
        val scale =applicationContext.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    @JvmStatic
    fun px2sp(pxValue: Float): Int {
        val scale = applicationContext.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    @JvmStatic
    fun sp2px(spValue: Float): Int {
        val scale = applicationContext.resources.displayMetrics.density
        return (spValue * scale + 0.5f).toInt()
    }
}