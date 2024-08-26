package com.caverock.androidsvg

import com.caverock.androidsvg.SVG
import com.xxf.ktx.dp


/**
 * 潘正炼创建于 2022/3/26 17:53
 */

object SVGKT {

    /**
     * 兼容 ex 单位
     * 1ex 通常是一个字体的一半大小，转化到Android 端就是 ex * 字体 * dpi倍率 / 2
     */
    fun updateExLength(svg: SVG) {

        val w = svg.rootElement.width
        val h = svg.rootElement.height
        if (w?.unit == SVG.Unit.ex) {
            val newWidth = (w.value * svg.rootElement.style.fontSize.value).dp / 2
            if (newWidth > 0) {
                svg.documentWidth = newWidth
            }
        }
        if (h?.unit == SVG.Unit.ex) {
            val newHeight = (h.value * svg.rootElement.style.fontSize.value).dp / 2
            if (newHeight > 0) {
                svg.documentHeight = newHeight
            }
        }
    }

}

