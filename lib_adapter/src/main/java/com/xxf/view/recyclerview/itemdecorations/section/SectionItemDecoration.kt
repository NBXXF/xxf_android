package com.xxf.view.recyclerview.itemdecorations.section

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.xxf.view.recyclerview.itemdecorations.section.SectionProvider
import com.xxf.view.recyclerview.itemdecorations.section.SectionBaseItemDecoration

/**
 * Description  聊天界面 时间分割线  间隔5分钟
 * author  youxuan  E-mail:xuanyouwu@163.com
 * date createTime：2017/4/29
 * version 1.0.0
 */
open class SectionItemDecoration(
    provider: SectionProvider?,
    sectionTextPaint: Paint,
    sectionBackgroundPaint: Paint,
    sectionOverTextPaint: Paint,
    sectionOverBackgroundPaint: Paint,
    dividerHeight: Float,
    paddingLeft: Float
) : SectionBaseItemDecoration(
    provider!!
) {
    /**
     * 分组的画笔
     */
    protected var sectionTextPaint = Paint()
    protected var sectionBackgroundPaint = Paint()

    /**
     * 分组悬浮的画笔
     */
    protected var sectionOverTextPaint = Paint()
    protected var sectionOverBackgroundPaint = Paint()

    //整个分割线高度
    protected var dividerHeight: Float

    /**
     * 左边距
     */
    protected var paddingLeft: Float

    constructor(
        provider: SectionProvider?,
        sectionTextPaint: Paint,
        sectionBackgroundPaint: Paint,
        dividerHeight: Float,
        paddingLeft: Float
    ) : this(
        provider,
        sectionTextPaint,
        sectionBackgroundPaint,
        sectionTextPaint,
        sectionBackgroundPaint,
        dividerHeight, paddingLeft
    ) {
    }

    override fun onDrawSection(
        c: Canvas?,
        parent: RecyclerView?,
        state: RecyclerView.State?,
        section: String?,
        child: View?
    ) {
        c!!.drawRect(
            child!!.left.toFloat(),
            child.top - dividerHeight,
            child.right.toFloat(),
            child.top.toFloat(),
            sectionBackgroundPaint
        )
        val txtStartX = paddingLeft
        val dividerCenterY = child.top - dividerHeight / 2
        c.drawText(
            section!!,
            txtStartX,
            dividerCenterY + sectionTextPaint.textSize * 0.25f,
            sectionTextPaint
        )
    }

    override fun onDrawSectionOver(
        c: Canvas,
        parent: RecyclerView,
        state: RecyclerView.State,
        section: String?
    ) {
        c.drawRect(
            parent.left.toFloat(),
            0f,
            parent.right.toFloat(),
            dividerHeight,
            sectionOverBackgroundPaint
        )
        val txtStartX = paddingLeft
        c.drawText(
            section!!,
            txtStartX,
            dividerHeight / 2 + sectionOverTextPaint.textSize * 0.25f,
            sectionOverTextPaint
        )
    }

    override fun getItemOffsets(
        outRect: Rect?,
        view: View?,
        parent: RecyclerView?,
        state: RecyclerView.State?,
        isSection: Boolean
    ) {
        if (isSection) {
            outRect!![0, dividerHeight.toInt(), 0] = 0
        } else {
            outRect!![0, 0, 0] = 0
        }
    }

    init {
        this.sectionTextPaint = sectionTextPaint
        this.sectionBackgroundPaint = sectionBackgroundPaint
        this.sectionOverTextPaint = sectionOverTextPaint
        this.sectionOverBackgroundPaint = sectionOverBackgroundPaint
        this.dividerHeight = dividerHeight
        this.paddingLeft = paddingLeft
    }
}