package com.xxf.utils

import android.content.Context
import android.util.TypedValue
import com.xxf.application.ApplicationProvider

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
            ApplicationProvider.applicationContext.resources.displayMetrics
    )

val Float.sp
    get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            this,
            ApplicationProvider.applicationContext.resources.displayMetrics
    )

val Int.dp
    get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            ApplicationProvider.applicationContext.resources.displayMetrics
    ).toInt()

val Int.sp
    get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            this.toFloat(),
            ApplicationProvider.applicationContext.resources.displayMetrics
    ).toInt()

object DensityUtil {
    @JvmStatic
    fun getScreenHeightPx(): Int {
        return ApplicationProvider.applicationContext.resources.displayMetrics.heightPixels
    }

    @JvmStatic
    fun getScreenWidthPx(): Int {
        return ApplicationProvider.applicationContext.resources.displayMetrics.widthPixels
    }

    @JvmStatic
    fun dip2px(dpValue: Float): Int {
        val scale = ApplicationProvider.applicationContext.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    @JvmStatic
    fun px2dip(pxValue: Float): Int {
        val scale =ApplicationProvider.applicationContext.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    @JvmStatic
    fun px2sp( pxValue: Float): Int {
        val scale =ApplicationProvider.applicationContext.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    @JvmStatic
    fun sp2px(spValue: Float): Int {
        val scale = ApplicationProvider.applicationContext.resources.displayMetrics.density
        return (spValue * scale + 0.5f).toInt()
    }
}