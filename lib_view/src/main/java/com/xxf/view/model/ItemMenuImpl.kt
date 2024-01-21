package com.xxf.view.model

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description
 */
data class ItemMenuImpl<T>(
    override var item: T,
    override var itemTitle: CharSequence? = null,
    override var itemIcon: Any? = null,
    override var isItemSelected: Boolean = false,
    override var isItemDisabled: Boolean = false
) : ItemMenu<T>