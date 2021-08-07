package com.xxf.utils

import android.content.Context

/**
 * Description 单位换算工具
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：16/6/23
 * version
 */
object DensityUtil {
    @JvmStatic
    fun getScreenHeightPx(context: Context): Int {
        return context.resources.displayMetrics.heightPixels
    }

    fun getScreenWidthPx(context: Context): Int {
        return context.resources.displayMetrics.widthPixels
    }

    @JvmStatic
    fun dip2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    fun px2dip(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    fun px2sp(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    fun sp2px(context: Context, spValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (spValue * scale + 0.5f).toInt()
    }
}