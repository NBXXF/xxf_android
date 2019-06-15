package com.xxf.view.recyclerview.adapter;

import android.view.View;

/**
 * Description
 *
 * author  youxuan  E-mail:xuanyouwu@163.com
 * date createTime：2017/10/3
 * version 2.1.0
 */
public interface OnItemLongClickListener {

    /**
     * @param adapter
     * @param holder
     * @param itemView
     * @param index    相对于List容器的位置
     * @return
     */
    boolean onItemLongClick(BaseRecyclerAdapter adapter, BaseViewHolder holder, View itemView, int index);
}