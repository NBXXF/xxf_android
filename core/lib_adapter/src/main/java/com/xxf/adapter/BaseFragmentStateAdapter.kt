package com.xxf.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * 避免重复刷新
 * class MyFragmentStateAdapter(
 *     fragmentActivity: FragmentActivity,
 *     private var dataList: MutableList<DataBean> // DataBean 有唯一 id
 * ) : BaseFragmentStateAdapter(fragmentActivity) {
 *
 *     init {
 *         setHasStableIds(true) // 启用稳定 ID
 *     }
 *
 *     // 重写：返回条目的唯一 ID（如 DataBean 的 id）
 *     override fun getItemId(position: Int): Long {
 *         return dataList[position].id // 假设 id 是 Long 类型的唯一标识
 *     }
 *
 *     // 可选：判断某个 ID 是否存在（优化刷新逻辑）
 *     override fun containsItem(itemId: Long): Boolean {
 *         return dataList.any { it.id == itemId }
 *     }
 *
 *     // 刷新数据（仅重建 ID 变化的 Fragment）
 *     fun refreshData(newData: List<DataBean>) {
 *         dataList.clear()
 *         dataList.addAll(newData)
 *         notifyDataSetChanged()
 *     }
 *
 *     override fun getItemCount(): Int = dataList.size
 *
 *     override fun createFragment(position: Int): Fragment {
 *         return MyFragment.newInstance(dataList[position])
 *     }
 *
 *     // 数据模型
 *     data class DataBean(val id: Long, val content: String)
 * }
 */
abstract class BaseFragmentStateAdapter<T> : FragmentStateAdapter {
    private val currentList: MutableList<T> = ArrayList()
    fun getCurrentList(): List<T> {
        return currentList
    }

    fun bindData(isRefresh: Boolean, items: List<T>) {
        if (items.isEmpty()) {
            return
        }
        if (isRefresh) {
            currentList.clear()
        }
        currentList.addAll(items)
        notifyDataSetChanged()
    }

    constructor(fragmentActivity: FragmentActivity) : super(fragmentActivity)
    constructor(fragment: Fragment) : super(fragment)
    constructor(fragmentManager: FragmentManager, lifecycle: Lifecycle) : super(
        fragmentManager,
        lifecycle
    )


    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return createFragmentItem(position, currentList[position % currentList.size])
    }

    abstract fun createFragmentItem(position: Int, item: T): Fragment
}