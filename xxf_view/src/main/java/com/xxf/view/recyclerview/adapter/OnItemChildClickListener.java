package com.xxf.view.recyclerview.adapter;

import android.view.View;

public interface OnItemChildClickListener {

    /**
     *
     * @param adapter
     * @param holder
     * @param childView
     * @param index 相对于List容器的位置
     */
    void onItemChildClick(BaseRecyclerAdapter adapter, BaseViewHolder holder, View childView, int index);
}