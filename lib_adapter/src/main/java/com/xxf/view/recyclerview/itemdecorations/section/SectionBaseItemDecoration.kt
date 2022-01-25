package com.xxf.view.recyclerview.itemdecorations.section

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

/**
 * Description  分组选择
 * author  youxuan  E-mail:xuanyouwu@163.com
 * date createTime：2017/4/29
 * version 1.0.0
 * 只支持线性布局
 */
abstract class SectionBaseItemDecoration(var provider: SectionProvider) :
    RecyclerView.ItemDecoration() {
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        val groupMap: TreeMap<Int, String> = provider.onProvideSection()
        if (parent.layoutManager is LinearLayoutManager) {
            val childCount = parent.childCount
            for (i in 0 until childCount) {
                val params = parent.getChildAt(i)
                    .layoutParams as RecyclerView.LayoutParams
                //int position = params.getViewLayoutPosition();
                val adapterPosition = params.viewAdapterPosition
                if (groupMap.containsKey(adapterPosition)) {
                    val child = parent.getChildAt(i)
                    val tag = groupMap[adapterPosition]
                    onDrawSection(c, parent, state, tag, child)
                }
            }
        }
    }

    /**
     * 会走section分组
     *
     * @param c
     * @param parent
     * @param state
     * @param section
     * @param child
     */
    abstract fun onDrawSection(
        c: Canvas?,
        parent: RecyclerView?,
        state: RecyclerView.State?,
        section: String?,
        child: View?
    )

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        if (parent.layoutManager is LinearLayoutManager) {
            val groupMap: TreeMap<Int, String> = provider.onProvideSection()
            if (parent.layoutManager is LinearLayoutManager) {
                val position =
                    (parent.layoutManager as LinearLayoutManager?)!!.findFirstVisibleItemPosition()
                val groupIndexList = ArrayList(groupMap.keys)
                var groupIndex = -1
                for (i in groupIndexList) {
                    if (i > position) {
                        break
                    }
                    groupIndex = i
                }
                if (groupMap.containsKey(groupIndex)) {
                    val tag = groupMap[groupIndex]
                    onDrawSectionOver(c, parent, state, tag)
                }
            }
        }
    }

    /**
     * 绘制悬浮
     *
     * @param c
     * @param parent
     * @param state
     * @param section
     */
    abstract fun onDrawSectionOver(
        c: Canvas,
        parent: RecyclerView,
        state: RecyclerView.State,
        section: String?
    )

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val params = view.layoutParams as RecyclerView.LayoutParams
        val adapterPosition = params.viewAdapterPosition
        val groupMap: TreeMap<Int, String> = provider.onProvideSection()
        getItemOffsets(outRect, view, parent, state, groupMap.containsKey(adapterPosition))
    }

    /**
     * 设置分组距离
     *
     * @param outRect
     * @param view
     * @param parent
     * @param state
     * @param isSection
     */
    abstract fun getItemOffsets(
        outRect: Rect?,
        view: View?,
        parent: RecyclerView?,
        state: RecyclerView.State?,
        isSection: Boolean
    )
}