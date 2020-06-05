package com.xxf.view.recyclerview.adapter;

import android.view.View;

import androidx.databinding.ViewDataBinding;

/**
 * Description
 *
 * author  youxuan  E-mail:xuanyouwu@163.com
 * date createTime：2017/10/3
 * version 2.1.0
 */
public interface OnItemClickListener<V extends ViewDataBinding,T> {

    /**
     * @param adapter
     * @param holder
     * @param itemView item根布局
     * @param t         对应的数据项
     * @param index    相对于List容器的位置
     */
    void onItemClick(XXFRecyclerAdapter<V,T> adapter, XXFViewHolder<V,T> holder, View itemView, int index,T t);
}
