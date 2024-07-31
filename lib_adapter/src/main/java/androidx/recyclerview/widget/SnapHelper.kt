package androidx.recyclerview.widget

/**
 * 获取已经附着的recyclerView
 */
val SnapHelper.attachedRecyclerView: RecyclerView?
    get() {
        return this.mRecyclerView
    }