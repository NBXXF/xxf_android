package com.xxf.mlkit.model

import android.graphics.Rect

open class FaceAnalyzerResult(val boundingBox: Rect, val faceBitmap: ByteArray)

/**
 * 按面积大小排序
 */
fun List<FaceAnalyzerResult>.sortAnalyzerResult(): List<FaceAnalyzerResult> {
    return this.sortedByDescending {
        /**
         * 计算矩形面积：width * height（假设使用 Android Rect）
         */
        it.boundingBox.width() * it.boundingBox.height()
    }
}