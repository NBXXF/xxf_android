package com.xxf.view.recyclerview.adapter;

import android.view.View;

import androidx.viewbinding.ViewBinding;

/**
 * Description
 * <p>
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2017/10/3
 * version 2.1.0
 */
public interface OnItemLongClickListener<V extends ViewBinding, T> {

    /**
     * @param adapter
     * @param holder
     * @param itemView item根布局
     * @param index    相对于List容器的位置
     * @param item        对应的数据项
     * @return
     */
    boolean onItemLongClick(BaseAdapter<V, T> adapter, XXFViewHolder<V, T> holder, View itemView, int index, T item);
}