package com.xxf.view.model

import com.xxf.model.SelectableEntity

/**
 * @author xuanyouwu
 * @email xuanyouwu@163.com
 * @time 2016-09-21 15:45
 * item 渲染控制模型
 */
interface ItemMenu<T> : SelectableEntity {
    /**
     * 实际数据
     */
    var item: T

    /**
     * 标题
     */
    var itemTitle: CharSequence?

    /**
     * 支持id int
     * 支持 string
     * gilde 就支持any 建议用glide加载 也不用转换
     *
     * @return
     */
    var itemIcon: Any?
}