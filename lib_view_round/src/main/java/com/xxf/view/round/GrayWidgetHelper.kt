package com.xxf.view.round

import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.view.View

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2018/10/20 16:15
 * Description ://变灰处理
 */
class GrayWidgetHelper {
    val mPaint by lazy { Paint() }
    val cm by lazy { ColorMatrix() }
    var isGrayColor = false

    fun draw(canvas: Canvas?) {
        canvas?.saveLayer(null, mPaint, Canvas.ALL_SAVE_FLAG)
    }

    fun dispatchDraw(canvas: Canvas?) {
        canvas?.saveLayer(null, mPaint, Canvas.ALL_SAVE_FLAG)
    }

    fun setGrayColor(grayColor: Boolean, view: View) {
        if (isGrayColor != grayColor) {
            isGrayColor = grayColor
            if (isGrayColor) {
                cm.setSaturation(0f)
                mPaint.colorFilter = ColorMatrixColorFilter(cm)
            } else {
                mPaint.colorFilter = null
            }
            view.invalidate()
        }
    }
}