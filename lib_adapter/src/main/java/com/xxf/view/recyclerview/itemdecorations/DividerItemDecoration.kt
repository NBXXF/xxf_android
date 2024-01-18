package com.xxf.view.recyclerview.itemdecorations

import android.graphics.Color
import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.xxf.view.recyclerview.itemdecorations.spacing.Spacing
import com.xxf.view.recyclerview.itemdecorations.spacing.SpacingItemDecoration

/**
 * 为[androidx.recyclerview.widget.RecyclerView] 添加分割线 支持线性和格子(达到等分) 且支持各种色彩填充和边距
 * recyclerView.addItemDivider {
 *    it.setSpacing(1)
 *      .setColor(1)
 * }
 *
 *
 * 技术选项篇
 * [androidx.recyclerview.widget.DividerItemDecoration] 系统这个 只能单向(组合加两条,但是不均分) 最后一条不能隐藏
 *
 * 不均分的算法 都是left right =space 或者 0.5space
 * https://github.com/yqritc/RecyclerView-FlexibleDivider 不能适配格子
 * https://github.com/ChoicesWang/RecyclerView_Divider 不均分 交叉的地方没有颜色 最后一条不能控制
 * https://github.com/nontravis/recycler-view-margin-decoration 均分了但是没有draw 就纯粹留空白
 * https://github.com/arjinmc/RecyclerViewDecoration.git 均分  格子判断 太宁散了
 * https://github.com/grzegorzojdana/SpacingItemDecoration   瀑布流 问题多, 格子布局 交叉的地方没有颜色 默认没有最后一条
 * https://github.com/hzl123456/SpacesItemDecoration 写法比较乱 最后一条变短了
 *
 */
class DividerItemDecoration {
    data class DividerConfig(
        var horizontalSpacing: Int = 0,//每两个项目之间的水平偏移量（以像素为单位）。
        var verticalSpacing: Int = 0,//每两个项目之间的垂直偏移量（以像素为单位）。
        var edges: Rect = Rect(),//可用于将项目从父边向父边中心移动的偏移集。可以把它想象成一个列表视图填充。以像素为单位的值是预期的。
        var item: Rect = Rect(),//添加到每个项目边缘的偏移量（以像素为单位）。
        var edgeColor: Int = Color.TRANSPARENT, //设置边距的颜色 前提是有edges
        var itemColor: Int = Color.TRANSPARENT, //当前item的背景色
        var horizontalColor: Int = Color.TRANSPARENT,//水平方向的颜色
        var verticalColor: Int = Color.TRANSPARENT //垂直方向的颜色)
    )

    /**
     * 保留
     * 主要是支持java 更方便
     */
    class Builder() {
        internal var dividerConfig: DividerConfig = DividerConfig()

        constructor(dividerConfig: DividerConfig) : this() {
            this.dividerConfig = dividerConfig
        }

        fun setSpacing(spacing: Int) = apply {
            dividerConfig.horizontalSpacing = spacing;
            dividerConfig.verticalSpacing = spacing;
        }

        fun setHorizontalSpacing(horizontalSpacing: Int) = apply {
            dividerConfig.horizontalSpacing = horizontalSpacing
        }

        fun setVerticalSpacing(verticalSpacing: Int) = apply {
            dividerConfig.verticalSpacing = verticalSpacing
        }

        fun setEdges(edges: Rect) = apply {
            dividerConfig.edges = edges
        }

        fun setItem(item: Rect) = apply {
            dividerConfig.item = item
        }

        fun setEdgeColor(edgeColor: Int) = apply {
            dividerConfig.edgeColor = edgeColor
        }

        fun setItemColor(itemColor: Int) = apply {
            dividerConfig.itemColor = itemColor
        }

        fun setColor(color: Int) = apply {
            dividerConfig.horizontalColor = color
            dividerConfig.verticalColor = color
        }

        fun setHorizontalColor(horizontalColor: Int) = apply {
            dividerConfig.horizontalColor = horizontalColor
        }

        fun setVerticalColor(verticalColor: Int) = apply {
            dividerConfig.verticalColor = verticalColor
        }

        fun build(): RecyclerView.ItemDecoration {
            return SpacingItemDecoration(
                Spacing(
                    dividerConfig.horizontalSpacing,
                    dividerConfig.verticalSpacing,
                    dividerConfig.edges,
                    dividerConfig.item
                )
            ).apply {
                this.drawingConfig = SpacingItemDecoration.DrawingConfig(
                    dividerConfig.edgeColor,
                    dividerConfig.itemColor,
                    dividerConfig.horizontalColor,
                    dividerConfig.verticalColor
                )
                this.isSpacingDrawingEnabled = !this.drawingConfig.isAllTransparent()
            }
        }
    }
}

private fun SpacingItemDecoration.DrawingConfig.isAllTransparent(): Boolean {
    return edgeColor == Color.TRANSPARENT
            && itemColor == Color.TRANSPARENT
            && horizontalColor == Color.TRANSPARENT
            && verticalColor == Color.TRANSPARENT
}

/**
 * 用法    recyclerView.addItemDivider {
 *          it.setSpacing(1)
 *         .setColor(1) }
 */
fun RecyclerView.addItemDivider(block:DividerItemDecoration.DividerConfig.() -> Unit): ItemDecoration {
    return DividerItemDecoration.Builder().apply {
        this.dividerConfig.apply(block)
    }.build().also {
        addItemDivider(it)
    }
}

fun RecyclerView.addItemDivider(config: DividerItemDecoration.DividerConfig): ItemDecoration {
    return DividerItemDecoration.Builder(config).build().also {
        addItemDivider(it)
    }
}

fun RecyclerView.addItemDivider(itemDecoration: RecyclerView.ItemDecoration) =
    addItemDecoration(itemDecoration)
