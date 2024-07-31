package com.xxf.view.recyclerview.snaphelper

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import androidx.recyclerview.widget.attachedRecyclerView

/**
 *  相比 [androidx.recyclerview.widget.SnapHelper.attachToRecyclerView] 更安全
 */
fun <T : SnapHelper> T.attachToRecyclerViewSafe(recyclerView: RecyclerView?) {
    if (recyclerView === this.attachedRecyclerView) {
        return
    }
    if (recyclerView != null) {
        /**
         * 避免抛出异常
         * Throws:
         * IllegalArgumentException – if there is already a RecyclerView. OnFlingListener attached to the provided RecyclerView.
         */
        recyclerView.onFlingListener = null
    }
    this.attachToRecyclerView(recyclerView)
}