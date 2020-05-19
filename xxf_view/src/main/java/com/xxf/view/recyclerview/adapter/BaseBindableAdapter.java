package com.xxf.view.recyclerview.adapter;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ViewDataBinding;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description databinding的adaptet
 */
public abstract class BaseBindableAdapter<V extends ViewDataBinding, T> extends BaseRecyclerAdapter<T> {
    public BaseBindableAdapter(@NonNull ObservableArrayList<T> data) {
        super(data);
    }

    public BaseBindableAdapter() {
    }

    /**
     * 创建vdb
     *
     * @param viewGroup
     * @param viewType
     * @param inflater
     * @return
     */
    protected abstract V onCreateBinding(LayoutInflater inflater, ViewGroup viewGroup, int viewType);

    @Override
    public final int bindView(int viewType) {
        throw new RuntimeException("dont use");
    }

    @Override
    public final BaseViewHolder onCreateHolder(ViewGroup viewGroup, int viewType) {
        ViewDataBinding inflate = onCreateBinding(LayoutInflater.from(viewGroup.getContext()), viewGroup, viewType);
        BaseViewHolder viewHolder = new BaseViewHolder(this, inflate.getRoot(), true);
        viewHolder.setBinding(inflate);
        return viewHolder;
    }


    @Override
    public final void onBindHolder(BaseViewHolder holder, @Nullable T t, int index) {
        this.onBindHolder(holder, (V) holder.getBinding(), t, index);
    }

    /**
     * bind vdb
     *
     * @param holder
     * @param binding
     * @param t
     * @param index
     */
    public abstract void onBindHolder(BaseViewHolder holder, V binding, @Nullable T t, int index);
}
