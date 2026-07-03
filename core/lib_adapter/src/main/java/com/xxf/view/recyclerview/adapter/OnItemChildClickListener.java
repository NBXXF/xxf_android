package com.xxf.view.recyclerview.adapter;

import android.view.View;

import androidx.viewbinding.ViewBinding;

public interface OnItemChildClickListener<V extends ViewBinding, T> {

    /**
     * @param adapter
     * @param holder    item holder
     * @param childView item 子view
     * @param index     相对于List容器的位置
     * @param item         对应的数据项
     */
    void onItemChildClick(BaseAdapter<V, T> adapter, XXFViewHolder<V, T> holder, View childView, int index, T item);
}