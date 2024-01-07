package com.xxf.view.recyclerview

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

fun RecyclerView.scrollToPositionWithOffset(position: Int, offset: Int) {
    val layoutManager = this.layoutManager
    if (layoutManager is LinearLayoutManager) {
        layoutManager.scrollToPositionWithOffset(position, offset)
    } else if (layoutManager is StaggeredGridLayoutManager) {
        layoutManager.scrollToPositionWithOffset(position, offset)
    }
}

fun RecyclerView.scrollToStartWithOffset(offset: Int) = scrollToPositionWithOffset(0, offset)

fun RecyclerView.scrollToEndWithOffset(offset: Int) =
    scrollToPositionWithOffset(adapter!!.itemCount - 1, offset)

fun RecyclerView.scrollToStart() = this.scrollToPosition(0)
fun RecyclerView.scrollToEnd() = this.scrollToPosition(adapter!!.itemCount - 1)

fun RecyclerView.smoothScrollToStart() =
    smoothScrollToStartPosition(0)

fun RecyclerView.smoothScrollToEnd() =
    smoothScrollToEndPosition(adapter!!.itemCount - 1)

fun RecyclerView.smoothScrollToStartPosition(position: Int) =
    smoothScrollToPosition(position, SNAP.SNAP_TO_START)

fun RecyclerView.smoothScrollToEndPosition(position: Int) =
    smoothScrollToPosition(position, SNAP.SNAP_TO_END)

fun RecyclerView.smoothScrollToPosition(position: Int, snapPreference: SNAP) =
    layoutManager?.let {
        val smoothScroller = LinearSmoothScroller(context, snapPreference)
        smoothScroller.targetPosition = position
        it.startSmoothScroll(smoothScroller)
    }

fun LinearSmoothScroller(context: Context, snapPreference: SNAP) =
    object : LinearSmoothScroller(context) {
        override fun getVerticalSnapPreference() = snapPreference.value
        override fun getHorizontalSnapPreference() = snapPreference.value
    }

enum class SNAP(var value: Int) {
    /**
     * 顶部
     */
    SNAP_TO_START(LinearSmoothScroller.SNAP_TO_START),

    /**
     * 尾部
     */
    SNAP_TO_END(LinearSmoothScroller.SNAP_TO_END),

    /**
     * 默认
     */
    SNAP_TO_ANY(LinearSmoothScroller.SNAP_TO_ANY)
}

fun RecyclerView.doAdapterDataObserver(block: () -> Unit): RecyclerView.AdapterDataObserver {
    return  this.adapter!!.doAdapterDataObserver (block)
}