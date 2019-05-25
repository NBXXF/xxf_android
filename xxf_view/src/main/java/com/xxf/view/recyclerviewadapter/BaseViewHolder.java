package com.xxf.view.recyclerviewadapter;

import android.databinding.ViewDataBinding;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

/**
 * Description
 * author  youxuan  E-mail:xuanyouwu@163.com
 * date createTime：2017/10/5
 * version 2.1.0
 */
public class BaseViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener, View.OnLongClickListener {
    private BaseRecyclerAdapter baseRecyclerAdapter;
    private SparseArray<View> holder = null;
    private ViewDataBinding binding;

    @Nullable
    public ViewDataBinding getBinding() {
        return binding;
    }

    public void setBinding(ViewDataBinding binding) {
        this.binding = binding;
    }

    public BaseViewHolder(BaseRecyclerAdapter baseRecyclerAdapter, View itemView, boolean bindItemClick) {
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
    public <T extends View> T obtainView(@IdRes int id) {
        if (null == holder) holder = new SparseArray<>();
        View view = holder.get(id);
        if (null != view) return (T) view;
        view = itemView.findViewById(id);
        if (null == view) return null;
        holder.put(id, view);
        return (T) view;
    }

    @Nullable
    public <T> T obtainView(@IdRes int id, Class<T> viewClazz) {
        View view = obtainView(id);
        if (null == view) return null;
        return (T) view;
    }


    public BaseViewHolder bindChildClick(@IdRes int id) {
        View view = obtainView(id);
        return bindChildClick(view);
    }

    /**
     * 子控件绑定局部点击事件
     *
     * @param v
     * @return
     */
    public BaseViewHolder bindChildClick(@NonNull View v) {
        if (v == null) return this;
        if (v == itemView) {
            throw new IllegalArgumentException("bindChildClick 不能传递item根布局!");
        }
        v.setOnClickListener(this);
        return this;
    }


    public BaseViewHolder bindChildLongClick(@IdRes int id) {
        View view = obtainView(id);
        return bindChildLongClick(view);
    }

    public BaseViewHolder bindChildLongClick(@NonNull View v) {
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
    public BaseViewHolder setText(@IdRes int id, CharSequence text) {
        View view = obtainView(id);
        if (view instanceof TextView) {
            ((TextView) view).setText(text);
        }
        return this;
    }


    @Override
    public boolean onLongClick(View v) {
        if (baseRecyclerAdapter.onItemLongClickListener != null && v.getId() == this.itemView.getId()) {
            return baseRecyclerAdapter.onItemLongClickListener.onItemLongClick(baseRecyclerAdapter, this, v, getIndex());
        } else if (baseRecyclerAdapter.onItemChildLongClickListener != null && v.getId() != this.itemView.getId()) {
            return baseRecyclerAdapter.onItemChildLongClickListener.onItemChildLongClick(baseRecyclerAdapter, this, v, getIndex());
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if (baseRecyclerAdapter.onItemClickListener != null && v.getId() == this.itemView.getId()) {
            baseRecyclerAdapter.onItemClickListener.onItemClick(baseRecyclerAdapter, this, v, getIndex());
        } else if (baseRecyclerAdapter.onItemChildClickListener != null && v.getId() != this.itemView.getId()) {
            baseRecyclerAdapter.onItemChildClickListener.onItemChildClick(baseRecyclerAdapter, this, v, getIndex());
        }
    }

}
