package com.xxf.media.preview.ui

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import com.github.chrisbanes.photoview.PhotoView

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2021/12/7
 * Description ://安全的photoView
 *
 * https://github.com/Baseflow/PhotoView/issues/606
 *
 * 存在崩溃的情况 主要是图片宽高比例太大导致
 *
 * https://github.com/bumptech/glide/issues/2990
 */
open class SafePhotoView : PhotoView {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attr: AttributeSet?) : super(context, attr)
    constructor(context: Context?, attr: AttributeSet?, defStyle: Int) : super(
        context,
        attr,
        defStyle
    )
//
//    init {
//        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//    }

    override fun onDraw(canvas: Canvas?) {
        try {
            super.onDraw(canvas)
        } catch (e: Throwable) {
            e.printStackTrace()
            System.gc()
        }
    }
}