package com.xxf.view.recyclerview.adapter;

import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

/**
 * Description
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2017/10/5
 * version 2.1.0
 */
public class XXFViewHolder<V extends ViewDataBinding & ViewBinding, T> extends RecyclerView.ViewHolder
        implements View.OnClickListener, View.OnLongClickListener {
    private XXFRecyclerAdapter<V, T> baseRecyclerAdapter;
    private SparseArray<View> holder = null;
    private ViewDataBinding binding;

    @Nullable
    public V getBinding() {
        return (V) binding;
    }

    public void setBinding(V binding) {
        this.binding = binding;
    }

    public XXFViewHolder(XXFRecyclerAdapter<V, T> baseRecyclerAdapter, View itemView, boolean bindItemClick) {
        super(itemView);
        this.baseRecyclerAdapter = baseRecyclerAdapter;
        if (bindItemClick) {
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }
    }

    /**
     * @return 相对于List容器的位置
     */
    public int getIndex() {
        return getAdapterPosition() - baseRecyclerAdapter.getHeaderCount();
    }

    /**
     * 获取子控件
     *
     * @param id
     * @param <T>
     * @return
     */
    @Nullable
    @Deprecated
    public <T extends View> T obtainView(@IdRes int id) {
        if (null == holder) holder = new SparseArray<>();
        View view = holder.get(id);
        if (null != view) return (T) view;
        view = itemView.findViewById(id);
        if (null == view) return null;
        holder.put(id, view);
        return (T) view;
    }


    public XXFViewHolder bindChildClick(@IdRes int id) {
        View view = obtainView(id);
        return bindChildClick(view);
    }

    /**
     * 子控件绑定局部点击事件
     *
     * @param v
     * @return
     */
    public XXFViewHolder bindChildClick(@NonNull View v) {
        if (v == null) return this;
        if (v == itemView) {
            throw new IllegalArgumentException("bindChildClick 不能传递item根布局!");
        }
        v.setOnClickListener(this);
        return this;
    }


    public XXFViewHolder bindChildLongClick(@IdRes int id) {
        View view = obtainView(id);
        return bindChildLongClick(view);
    }

    public XXFViewHolder bindChildLongClick(@NonNull View v) {
        if (v == null) return this;
        if (v == itemView) {
            throw new IllegalArgumentException("bindChildLongClick 不能传递item根布局");
        }
        v.setOnLongClickListener(this);
        return this;
    }

    /**
     * 文本控件赋值
     *
     * @param id
     * @param text
     */
    public XXFViewHolder setText(@IdRes int id, CharSequence text) {
        View view = obtainView(id);
        if (view instanceof TextView) {
            ((TextView) view).setText(text);
        }
        return this;
    }


    @Override
    public boolean onLongClick(View v) {
        if (baseRecyclerAdapter.onItemLongClickListener != null && v.getId() == this.itemView.getId()) {
            return baseRecyclerAdapter.onItemLongClickListener.onItemLongClick(baseRecyclerAdapter, this, v, getIndex(), baseRecyclerAdapter.getItem(getIndex()));
        } else if (baseRecyclerAdapter.onItemChildLongClickListener != null && v.getId() != this.itemView.getId()) {
            return baseRecyclerAdapter.onItemChildLongClickListener.onItemChildLongClick(baseRecyclerAdapter, this, v, getIndex(), baseRecyclerAdapter.getItem(getIndex()));
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if (baseRecyclerAdapter.onItemClickListener != null && v.getId() == this.itemView.getId()) {
            baseRecyclerAdapter.onItemClickListener.onItemClick(baseRecyclerAdapter, this, v, getIndex(), baseRecyclerAdapter.getItem(getIndex()));
        } else if (baseRecyclerAdapter.onItemChildClickListener != null && v.getId() != this.itemView.getId()) {
            baseRecyclerAdapter.onItemChildClickListener.onItemChildClick(baseRecyclerAdapter, this, v, getIndex(), baseRecyclerAdapter.getItem(getIndex()));
        }
    }

}
