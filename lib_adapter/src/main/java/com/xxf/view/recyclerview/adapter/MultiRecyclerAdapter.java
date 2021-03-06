package com.xxf.view.recyclerview.adapter;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ViewDataBinding;
import androidx.viewbinding.ViewBinding;

import com.xxf.view.recyclerview.SafeObservableArrayList;

/**
 * Description 多布局的适配器
 *
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2017/10/4
 * version 2.1.0
 */
public abstract class MultiRecyclerAdapter<V extends ViewDataBinding & ViewBinding,T extends MultiViewEntity> extends XXFRecyclerAdapter<V,T> {
    public MultiRecyclerAdapter() {
    }

    public MultiRecyclerAdapter(@NonNull SafeObservableArrayList<T> data) {
        super(data);
    }

    @Override
    public final int getViewType(int index) {
        T item = getItem(index);
        return item != null ? item.getViewType() : super.getViewType(index);
    }
}
