package com.xxf.view.recyclerview.adapter;

import androidx.annotation.NonNull;
import androidx.viewbinding.ViewBinding;

import java.util.ArrayList;


/**
 * Description 多布局的适配器
 *
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2017/10/4
 * version 2.1.0
 */
public abstract class MultiRecyclerAdapter<V extends ViewBinding, T extends MultiViewEntity> extends XXFRecyclerAdapter<V, T> {
    public MultiRecyclerAdapter() {
    }

    public MultiRecyclerAdapter(@NonNull ArrayList<T> data) {
        super(data);
    }

    @Override
    public int getItemViewType(int position) {
        T item = getItem(position);
        return item != null ? item.getViewType() : super.getItemViewType(position);
    }
}
