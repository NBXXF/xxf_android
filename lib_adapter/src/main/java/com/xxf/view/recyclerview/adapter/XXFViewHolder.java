package com.xxf.view.recyclerview.adapter;

import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.InnerViewHolder;
import androidx.viewbinding.ViewBinding;


/**
 * Description
 *
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2017/10/5
 * version 2.1.0
 */
public class XXFViewHolder<V extends ViewBinding, T> extends InnerViewHolder
        implements View.OnClickListener, View.OnLongClickListener {
    private BaseAdapter<V, T> baseAdapter;
    private SparseArray<View> holder = null;
    private ViewBinding binding;

    @Nullable
    public V getBinding() {
        return (V) binding;
    }

    public void setBinding(V binding) {
        this.binding = binding;
    }


    public BaseAdapter<V, T> getRecyclerAdapter() {
        return this.baseAdapter;
    }

    /**
     * 绑定的构造方法
     *
     * @param baseAdapter
     * @param binding
     * @param bindItemClick
     */
    public XXFViewHolder(BaseAdapter<V, T> baseAdapter, V binding, boolean bindItemClick) {
        this(baseAdapter, binding.getRoot(), bindItemClick);
        this.setBinding(binding);
    }

    /**
     * 普通view的构造方法
     *
     * @param baseAdapter
     * @param itemView
     * @param bindItemClick
     */
    public XXFViewHolder(BaseAdapter<V, T> baseAdapter, View itemView, boolean bindItemClick) {
        super(itemView);
        this.baseAdapter = baseAdapter;
        if (bindItemClick) {
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }
    }

    /**
     * @return 相对于List容器的位置
     */
    public int getIndex() {
        return getAdapterPosition();
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
        if (v.getId() == this.itemView.getId()) {
            return baseAdapter.callOnItemLongClick(this, v, getIndex());
        } else if (v.getId() != this.itemView.getId()) {
            return baseAdapter.callOnItemChildLongClick(this, v, getIndex());
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == this.itemView.getId()) {
            baseAdapter.callOnItemClick(this, v, getIndex());
        } else if (v.getId() != this.itemView.getId()) {
            baseAdapter.callOnItemChildClick(this, v, getIndex());
        }
    }
}
