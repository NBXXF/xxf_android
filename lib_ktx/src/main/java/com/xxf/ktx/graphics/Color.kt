package com.xxf.ktx.graphics

import android.graphics.Color
import android.os.Build
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.annotation.RequiresApi
import androidx.core.graphics.ColorUtils

/**
 * 0.0：完全透明（颜色不可见）。
 * 1.0：完全不透明（颜色完全可见）。
 */
@RequiresApi(Build.VERSION_CODES.O)
fun Color.alpha(@FloatRange(from = 0.0, to = 1.0) alpha: Float): Color {
    return Color.valueOf(ColorUtils.setAlphaComponent(this.toArgb(), (alpha * 255).toInt()));
}

/**
 * Opacity = 0
 * 元素不可见，但仍占据布局空间（如 HTML 中的元素仍会占位，Android 视图仍会响应触摸事件）。
 * 示例：隐藏一个按钮但保留其占位区域。
 * Opacity = 1
 * 元素完全可见，正常显示所有内容（颜色、图片、文字等）。
 */
@RequiresApi(Build.VERSION_CODES.O)
fun Color.opacity(@FloatRange(from = 0.0, to = 1.0) opacity: Float): Color {
    return Color.valueOf(ColorUtils.setAlphaComponent(this.toArgb(), (opacity * 255).toInt()));
}


/**
 * 0：完全透明（颜色不可见）。
 * 255：完全不透明（颜色完全可见）。
 */
@RequiresApi(Build.VERSION_CODES.O)
fun Color.alphaComponent(@IntRange(from = 0, to = 255) alpha: Int): Color {
    return Color.valueOf(ColorUtils.setAlphaComponent(this.toArgb(), alpha))
}
