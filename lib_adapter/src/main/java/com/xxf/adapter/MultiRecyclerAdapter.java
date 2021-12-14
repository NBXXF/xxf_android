package com.xxf.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.viewbinding.ViewBinding;

import com.xxf.view.recyclerview.adapter.MultiViewEntity;
import com.xxf.view.recyclerview.adapter.XXFRecyclerAdapter;

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

    @Override
    public int getItemViewType(int position) {
        T item = getItem(position);
        return item != null ? item.getViewType() : super.getItemViewType(position);
    }
}
