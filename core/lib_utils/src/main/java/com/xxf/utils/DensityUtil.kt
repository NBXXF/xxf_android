package com.xxf.utils


import com.xxf.application.applicationContext

/**
 * Description 单位换算工具
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：16/6/23
 * version
 */
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