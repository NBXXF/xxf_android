package com.xxf.view.recyclerview.layoutmanager

import android.content.Context
import android.util.Range
import android.util.Size
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.max
import kotlin.math.min

/**
 * 格子布局 手机 pad 横竖自适应
 * 自动计算格子数量,注意两个方向 排列计算是不一样的 跟orientation相关
 */
open class AutoFitGridLayoutManager(
    private val context: Context,
    private val adaptConfig: AdaptConfig
) :
    GridLayoutManager(context, 1) {

    constructor(
        context: Context,
        block: (AdaptConfig) -> Unit
    ) : this(context, AdaptConfig(expectedSize = Size(0, 0)).apply { block(this) })

    /**
     * 保留
     * 主要是支持java 更方便
     */
    class Builder(
        private var context: Context,
        /**
         * 期望的大小
         */
        private var expectedSize: Size
    ) {
        private var adaptConfig: AdaptConfig = AdaptConfig(expectedSize)

        fun setSpacing(spacing: Size): Builder {
            adaptConfig.spacing = spacing
            return this
        }

        fun setColumnRange(columnRange: Range<Int>): Builder {
            adaptConfig.columnRange = columnRange
            return this
        }

        fun setRowRange(rowRange: Range<Int>): Builder {
            adaptConfig.rowRange = rowRange
            return this
        }

        fun build(): AutoFitGridLayoutManager {
            return AutoFitGridLayoutManager(context, adaptConfig)
        }
    }

    data class AdaptConfig(
        /**
         * 期望的大小
         */
        var expectedSize: Size,
        /**
         * 间距
         */
        var spacing: Size = Size(0, 0),
        /**
         * 列的区间限制
         */
        var columnRange: Range<Int> = Range<Int>(1, Int.MAX_VALUE),
        /**
         * 行的区间限制
         */
        var rowRange: Range<Int> = Range<Int>(1, Int.MAX_VALUE)
    )

    class GridCalculator(private var totalSize: Size, private var adaptConfig: AdaptConfig) {
        fun calculateByWidth(): Int {
            return adaptSpanCount(
                totalSize.width,
                adaptConfig.expectedSize.width,
                adaptConfig.spacing.width,
                adaptConfig.columnRange
            )
        }

        fun calculateByHeight(): Int {
            return adaptSpanCount(
                totalSize.height,
                adaptConfig.expectedSize.height,
                adaptConfig.spacing.height,
                adaptConfig.rowRange
            )
        }

        private fun adaptSpanCount(
            parentWidth: Int,
            expectedColumnWidth: Int,
            spacingWidth: Int,
            spanRange: Range<Int>
        ): Int {
            var spanCount = parentWidth / expectedColumnWidth
            while (spanCount > 1 && spanCount * expectedColumnWidth + (spanCount - 1) * spacingWidth > parentWidth) {
                spanCount--
            }
            return min(max(spanCount, spanRange.lower), spanRange.upper)
        }
    }

    override fun onLayoutCompleted(state: RecyclerView.State) {
        super.onLayoutCompleted(state)
        autoFitSpan()
    }

    /**
     * 自动适配span 的数量
     */
    protected open fun autoFitSpan() {
        val totalSize = Size(
            width - paddingStart - paddingEnd,
            height - paddingTop - paddingBottom
        )
        spanCount = if (orientation == VERTICAL) {
            GridCalculator(
                totalSize,
                adaptConfig
            ).calculateByWidth()
        } else {
            GridCalculator(
                totalSize,
                adaptConfig
            ).calculateByHeight()
        }
    }
}