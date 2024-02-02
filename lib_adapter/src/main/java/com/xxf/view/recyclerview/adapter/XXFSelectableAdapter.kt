package com.xxf.view.recyclerview.adapter

import android.annotation.SuppressLint
import androidx.viewbinding.ViewBinding
import com.xxf.model.SelectableEntity
import com.xxf.model.clearSelected
import com.xxf.model.getFirstSelected
import com.xxf.model.getSelectedItems
import com.xxf.model.setItemSelect
import java.io.Serializable

/**
 * 提供直接操作的
 */
/**
 * 清除选中的items
 */
@SuppressLint("NotifyDataSetChanged")
fun <V : ViewBinding, T : SelectableEntity> BaseAdapter<V, T>.clearSelectedItems() {
    changeRefresh(this.currentList.toList().apply {
        this.clearSelected()
    })
}

/**
 * 选中item
 *
 * @param select
 * @param index
 */
@SuppressLint("NotifyDataSetChanged")
fun <V : ViewBinding, T : SelectableEntity> BaseAdapter<V, T>.setItemSelect(
    select: Boolean,
    index: Int,
    selectType: SelectType = SelectType.SELECT_TYPE_SINGLE
) {
    changeRefresh(this.currentList.toList().apply {
        if (selectType == SelectType.SELECT_TYPE_SINGLE) {
            this.clearSelected()
        }
        this.setItemSelect(select, index)
    })
}

/**
 * 反选item
 *
 * @param index
 */
@SuppressLint("NotifyDataSetChanged")
fun <V : ViewBinding, T : SelectableEntity> BaseAdapter<V, T>.toggleItemSelect(
    index: Int,
    selectType: SelectType = SelectType.SELECT_TYPE_SINGLE
) {
    val item = getItem(index)
    this.setItemSelect(item?.isItemSelected != true, index, selectType)
}

/**
 * 获取全部选中的
 */
fun <V : ViewBinding, T : SelectableEntity> BaseAdapter<V, T>.getSelectedItems(): List<T> {
    return this.currentList.getSelectedItems()
}

/**
 * 获取第一个选中的
 */
fun <V : ViewBinding, T : SelectableEntity> BaseAdapter<V, T>.getSelectedItem(): T? {
    return this.currentList.getFirstSelected()
}

@SuppressLint("NotifyDataSetChanged")
private fun <V : ViewBinding, T : SelectableEntity> BaseAdapter<V, T>.changeRefresh(newResults: List<T>) {
    //底层可能有diff 这样重新绑定一次 好一些 有效避免刷新
    this.bindData(true, newResults)
}

enum class SelectType : Serializable {
    SELECT_TYPE_SINGLE,
    SELECT_TYPE_MULTIPLE
}