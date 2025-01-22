package com.xxf.mlkit.overlay

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import com.xxf.ktx.dp

/**
 * @ClassName: OutlineGraphic
 * @Description: 轮廓 就行了
 * @Author: xuanyouwu@163.com 17611639080
 * @Date: 2023/8/24 13:42
 */
class OutlineGraphic(
    overlay: GraphicOverlay,
    private val detectedRect: Rect,
    private val imageRect: Rect,
    private val selectedColor: Int = Color.YELLOW,
    private val strokeWidth: Float = BOX_STROKE_WIDTH,
) : GraphicOverlay.Graphic(overlay) {
    companion object {
        private val BOX_STROKE_WIDTH = 2.0f.dp
    }

    private val outlinePaint: Paint = Paint()
    private val idPaint: Paint
    private val boxPaint: Paint

    init {
        outlinePaint.color = selectedColor

        idPaint = Paint()
        idPaint.color = selectedColor

        boxPaint = Paint()
        boxPaint.color = selectedColor
        boxPaint.style = Paint.Style.STROKE
        boxPaint.strokeWidth = strokeWidth
    }

    override fun draw(canvas: Canvas?) {
        val rect = calculateRect(
            imageRect.height().toFloat(),
            imageRect.width().toFloat(),
            detectedRect
        )
        canvas?.drawRect(rect, boxPaint)
    }
}