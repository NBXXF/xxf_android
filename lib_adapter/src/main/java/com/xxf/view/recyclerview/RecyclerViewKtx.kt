package com.xxf.view.recyclerview

import android.content.Context
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.xxf.ktx.getTag
import com.xxf.ktx.hideKeyboard
import com.xxf.ktx.internals.KeyboardHiddenTouchListener
import com.xxf.ktx.isKeyboardHiddenInTouchMode
import com.xxf.ktx.setTag

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

fun <VH : RecyclerView.ViewHolder, T : RecyclerView.Adapter<VH>> RecyclerView.doAdapterDataObserver(
    block: T.() -> Unit
): RecyclerView.AdapterDataObserver {
    @Suppress("UNCHECKED_CAST")
    return (this.adapter as T).doAdapterDataObserver(block)
}

/**
 * 获取所有分割线
 */
fun RecyclerView.getItemDecorations(): List<ItemDecoration> {
    val itemDecorationCount = this.itemDecorationCount
    val itemDecorations: MutableList<ItemDecoration> = mutableListOf()
    repeat(itemDecorationCount) {
        itemDecorations.add(getItemDecorationAt(it))
    }
    return itemDecorations
}

/**
 * 移除所有分割线
 */
fun RecyclerView.removeItemDecorations() {
    getItemDecorations().onEach {
        removeItemDecoration(it)
    }
}

/**
 * 获取adapter
 * 没有,或者类型不匹配 都将初始化
 */
inline fun <reified T : Adapter<VH>, VH> RecyclerView.getAdapterIfNeeded(initializer: RecyclerView.() -> T): T {
    return if (adapter is T) {
        adapter as T
    } else {
        return initializer().apply {
            adapter = this
        }
    }
}

/**
 * 隐藏键盘
 */
inline var RecyclerView.isKeyboardHiddenInTouchMode: Boolean
    get() {
        return this.getTag<OnItemTouchListener>(RecyclerView::isKeyboardHiddenInTouchMode.name) != null
    }
    set(value) {
        var itemTouchListener =
            this.getTag<OnItemTouchListener>(RecyclerView::isKeyboardHiddenInTouchMode.name)
        if (value) {
            if (itemTouchListener == null) {
                itemTouchListener = object : OnItemTouchListener {
                    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                        if (e.action == MotionEvent.ACTION_DOWN) {
                            rv.hideKeyboard()
                        }
                        return false
                    }

                    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
                    }

                    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

                    }
                }.also { it ->
                    this.setTag(RecyclerView::isKeyboardHiddenInTouchMode.name, it)
                }
            }
            this.removeOnItemTouchListener(itemTouchListener)
            this.addOnItemTouchListener(itemTouchListener)
        } else {
            itemTouchListener?.let { this.removeOnItemTouchListener(it) }
            this.setTag(RecyclerView::isKeyboardHiddenInTouchMode.name, Unit)
        }
    }