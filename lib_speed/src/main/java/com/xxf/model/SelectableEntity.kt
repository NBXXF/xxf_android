package com.xxf.model

import java.io.Serializable

/**
 * Description  带选中属性的item实体
 * 解决常见业务选中 单选 多选的 定义 和操作方法
 *
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2017/10/5
 * version 2.1.0
 */
interface SelectableEntity : Serializable {
    /**
     * 是否选中
     *
     * @return
     */
    var isItemSelected: Boolean

    /**
     * 是否禁用
     */
    var isItemDisabled: Boolean
}

class SimpleSelectableEntity(
    override var isItemSelected: Boolean = false,
    override var isItemDisabled: Boolean = false
) : SelectableEntity

@Deprecated("过时,直接访问字段,这是为了兼容老版本", ReplaceWith("直接访问字段"))
fun <T : SelectableEntity> T.setItemSelect(select: Boolean) {
    if (!isItemDisabled) {
        this.isItemSelected = select
    }
}

@Deprecated("过时,直接访问字段,这是为了兼容老版本", ReplaceWith("直接访问字段"))
fun <T : SelectableEntity> T.toggleItemSelect() {
    if (!isItemDisabled) {
        this.isItemSelected = !this.isItemSelected
    }
}

/**
 * 是否有选中
 */
fun <T : SelectableEntity> List<T>.hasSelected(): Boolean {
    return this.indexOfFirst {
        it.isItemSelected
    } >= 0
}


/**
 * 获取第一个选中的
 */
fun <T : SelectableEntity> List<T>.getFirstSelected(): T? {
    return this.firstOrNull {
        it.isItemSelected
    }
}

/**
 * 获取全部选中
 */
fun <T : SelectableEntity> List<T>.getSelectedItems(): List<T> {
    return this.filter {
        it.isItemSelected
    }
}

/**
 * 清除全部选中
 */
fun <T : SelectableEntity> List<T>.clearSelected() {
    this.onEach {
        it.isItemSelected = false
    }
}

/**
 * 单选或者多选
 */
fun <T : SelectableEntity> List<T>.selectItems(vararg selected: T) {
    val toList = selected.toList()
    this.onEach {
        it.isItemSelected = toList.contains(it)
    }
}

fun <T : SelectableEntity> List<T>.setItemSelect(select: Boolean, index: Int) {
    this[index].apply {
        this.isItemSelected = select
    }
}



