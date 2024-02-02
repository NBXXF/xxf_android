package com.xxf.view.recyclerview.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.xxf.model.SelectableEntity
import com.xxf.model.clearSelected
import com.xxf.model.getFirstSelected
import com.xxf.model.getSelectedItems
import com.xxf.model.setItemSelect
import com.xxf.view.recyclerview.doWithoutAnimation
import java.io.Serializable

/**
 * 在没有动画的事务中执行
 */
fun <V : ViewBinding, D : SelectableEntity, T : BaseAdapter<V, D>> T.doWithoutAnimation(block: T.() -> Unit) {
    if (this.recyclerView != null) {
        this.recyclerView.doWithoutAnimation {
            this@doWithoutAnimation.apply(block)
        }
    } else {
        block()
    }
}

/**
 * 在没有动画的事务中执行
 */
fun <V : ViewBinding, D : SelectableEntity, T : BaseAdapter<V, D>> T.withoutAnimation(block: T.() -> Unit) {
    this.doWithoutAnimation(block)
}
/**
 * 提供直接操作的
 */
/**
 * 清除选中的items
 */
@SuppressLint("NotifyDataSetChanged")
fun <V : ViewBinding, T : SelectableEntity> BaseAdapter<V, T>.clearSelectedItems() {
    changeRefresh(this.currentList.map {
        it.isItemSelected
    }, this.currentList.apply {
        this.clearSelected()
    }.map {
        it.isItemSelected
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
    changeRefresh(
        this.currentList.map {
            it.isItemSelected
        },
        this.currentList.apply {
            if (selectType == SelectType.SELECT_TYPE_SINGLE) {
                this.clearSelected()
            }
            this.setItemSelect(select, index)
        }.map {
            it.isItemSelected
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
private fun <V : ViewBinding, T : SelectableEntity> BaseAdapter<V, T>.changeRefresh(
    beforeChanges: List<Boolean>,
    afterChanges: List<Boolean>
) {
    this.doWithoutAnimation {
        beforeChanges.forEachIndexed { index, b ->
            if (afterChanges[index] != b && this.recyclerView.findViewHolderForAdapterPosition(index) != null) {
                (this as? RecyclerView.Adapter<*>)?.notifyItemChanged(index)
            }
        }
    }
}

enum class SelectType : Serializable {
    SELECT_TYPE_SINGLE,
    SELECT_TYPE_MULTIPLE
}