package androidx.recyclerview.widget

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：6/26/21
 * Description : 链接内部非公开方法
 */
open class InnerRecyclerViewPool : RecyclerView.RecycledViewPool() {
    private val listeners: MutableList<OnAdapterChangedListener> = mutableListOf()

    interface OnAdapterChangedListener {
        /**
         * @param compatibleWithPrevious 如果 oldAdapter 和 newAdapter 都使用相同的 ViewHolder 和视图类型，则为 True。
         */
        fun onAdapterChanged(
            oldAdapter: RecyclerView.Adapter<*>?,
            newAdapter: RecyclerView.Adapter<*>?,
            compatibleWithPrevious: Boolean
        )
    }

    open fun addAdapterChangedListener(listener: OnAdapterChangedListener) {
        if (listeners.contains(listener)) {
            listeners.add(listener)
        }
    }

    open fun removeAdapterChangedListener(listener: OnAdapterChangedListener) {
        listeners.remove(listener)
    }

    override fun onAdapterChanged(
        oldAdapter: RecyclerView.Adapter<*>?,
        newAdapter: RecyclerView.Adapter<*>?,
        compatibleWithPrevious: Boolean
    ) {
        super.onAdapterChanged(oldAdapter, newAdapter, compatibleWithPrevious)
        listeners.forEach {
            it.onAdapterChanged(oldAdapter, newAdapter, compatibleWithPrevious)
        }
    }
}