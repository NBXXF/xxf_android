package com.xxf.mlkit.model

import android.graphics.Rect

open class BarcodeAnalyzerResult(val boundingBox: Rect, val displayValue: String)

/**
 * 按面积大小排序
 */
fun List<BarcodeAnalyzerResult>.sortAnalyzerResult(): List<BarcodeAnalyzerResult> {
    return this.sortedByDescending {
        /**
         * 计算矩形面积：width * height（假设使用 Android Rect）
         */
        it.boundingBox.width() * it.boundingBox.height()
    }
}

/**
 * 筛选有用的结果
 */
fun List<BarcodeAnalyzerResult>.filterAnalyzerResult(): List<BarcodeAnalyzerResult> {
    return this.filter { it.displayValue.isNotEmpty() }
}