package com.xxf.view.recyclerview.adapter;

import android.view.View;

public interface OnItemChildLongClickListener {
    /**
     *
     * @param adapter
     * @param holder
     * @param childView
     * @param index  相对于List容器的位置
     * @return
     */
    boolean onItemChildLongClick(BaseRecyclerAdapter adapter, BaseViewHolder holder, View childView, int index);
}