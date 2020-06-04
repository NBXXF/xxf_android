package com.xxf.view.recyclerview.adapter;

import android.view.View;

import androidx.databinding.ViewDataBinding;

public interface OnItemChildLongClickListener<V extends ViewDataBinding,T> {
    /**
     *
     * @param adapter
     * @param holder
     * @param childView
     * @param index  相对于List容器的位置
     * @return
     */
    boolean onItemChildLongClick(XXFRecyclerAdapter<V,T> adapter, XXFViewHolder<V,T> holder, View childView, int index);
}