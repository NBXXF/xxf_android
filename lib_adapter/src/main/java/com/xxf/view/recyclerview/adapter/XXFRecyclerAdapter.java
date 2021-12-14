package com.xxf.view.recyclerview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.CheckResult;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * ClassName BaseRecyclerAdapter
 * Description
 * Company
 *
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2015/9/10 10:05
 * version
 */
public abstract class XXFRecyclerAdapter<V extends ViewBinding, T>
        extends ListAdapter<T, XXFViewHolder<V, T>> {

    private RecyclerView attachedRecyclerView;

    public XXFRecyclerAdapter() {
        super(new DiffUtil.ItemCallback<T>() {
            @Override
            public boolean areItemsTheSame(@NonNull T oldItem, @NonNull T newItem) {
                return false;
            }

            @Override
            public boolean areContentsTheSame(@NonNull T oldItem, @NonNull T newItem) {
                return false;
            }
        });
    }

    public XXFRecyclerAdapter(@NonNull DiffUtil.ItemCallback<T> diffCallback) {
        super(diffCallback);
    }

    public XXFRecyclerAdapter(@NonNull AsyncDifferConfig<T> config) {
        super(config);
    }

    public List<T> getData() {
        return getCurrentList();
    }

    public int getDataSize() {
        return getCurrentList().size();
    }

    public boolean isDataEmpty() {
        return getDataSize() <= 0;
    }

    /**
     * @param isRefresh 是否下拉刷新
     * @param datas
     * @return
     */
    public boolean bindData(boolean isRefresh, @NonNull List<T> datas) {
        if (isRefresh) {//下拉刷新
            submitList(datas);
            return true;
        } else {
            //上拉加载 不能为空
            if (checkList(datas)) {
                ArrayList<T> totalList = new ArrayList<T>(getCurrentList());
                totalList.addAll(datas);
                submitList(totalList);
                return true;
            }
        }
        return false;
    }


    public void clearData() {
        if (!isDataEmpty()) {
            submitList(new ArrayList<>());
        }
    }

    /**
     * 检查index是否有效
     *
     * @param index
     * @return true 有效 false 无效
     */
    private boolean checkIndex(@IntRange(from = 0) int index) {
        return index >= 0 && index < getDataSize();
    }

    /**
     * 检查list是否有效
     *
     * @param datas
     * @return true 不空
     */
    private boolean checkList(Collection<? extends T> datas) {
        return datas != null && !datas.isEmpty();
    }

    /**
     * @param index 相对于List的位置
     * @return
     */
    public T getItem(@IntRange(from = 0) int index) {
        if (checkIndex(index)) {
            return super.getItem(index);
        }
        return null;
    }

    /**
     * 获取角标
     *
     * @param t
     * @return
     */
    public int getIndex(@NonNull T t) {
        return getCurrentList().indexOf(t);
    }

    /**
     * 获取指定item 的viewholder 一定要在缓存中的,一定要recycler.setAdapter 之后获取,否则返回null
     *
     * @param index
     * @return
     */
    @Nullable
    @CheckResult
    public XXFViewHolder<V, T> getViewHolder(int index) {
        if (checkIndex(index)) {
            try {
                return (XXFViewHolder<V, T>) this.attachedRecyclerView.findViewHolderForAdapterPosition(index);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * @param index 相对于List的位置
     * @param t
     * @return
     */
    public boolean addItem(@IntRange(from = 0) int index, @NonNull T t) {
        if (checkIndex(index)) {
            ArrayList<T> ts = new ArrayList<>(getCurrentList());
            ts.add(index, t);
            submitList(ts);
            return true;
        }
        return false;
    }

    public boolean addItems(@IntRange(from = 0) int index, @NonNull List<? extends T> datas) {
        if (checkList(datas)
                && checkIndex(index)) {
            ArrayList<T> ts = new ArrayList<>(getCurrentList());
            if (ts.addAll(index, datas)) {
                submitList(ts);
                return true;
            }
        }
        return false;
    }

    public boolean addItems(@NonNull Collection<? extends T> datas) {
        if (checkList(datas)) {
            ArrayList<T> ts = new ArrayList<>(getCurrentList());
            if (ts.addAll(datas)) {
                submitList(ts);
                return true;
            }
        }
        return false;
    }

    public boolean addItem(@NonNull T t) {
        ArrayList<T> ts = new ArrayList<>(getCurrentList());
        ts.add(t);
        submitList(ts);
        return true;
    }

    /**
     * 更新item
     *
     * @param t
     * @return
     */
    public boolean updateItem(@NonNull T t) {
        ArrayList<T> ts = new ArrayList<>(getCurrentList());
        int i = ts.indexOf(t);
        if (i >= 0) {
            ts.set(i, t);
            submitList(ts);
            return true;
        }
        return false;
    }

    /**
     * 更新item
     *
     * @param t
     * @return
     */
    public boolean updateItem(int index, @NonNull T t) {
        if (checkIndex(index)) {
            ArrayList<T> ts = new ArrayList<>(getCurrentList());
            ts.set(index, t);
            submitList(ts);
            return true;
        }
        return false;
    }

    /**
     * @param index 相对于List的位置
     * @return
     */
    public boolean removeItem(@IntRange(from = 0) int index) {
        if (checkIndex(index)) {
            ArrayList<T> ts = new ArrayList<>(getCurrentList());
            if (ts.remove(index) != null) {
                submitList(ts);
                return true;
            }
        }
        return false;
    }


    /**
     * 移除item
     *
     * @param t
     * @return
     */
    public boolean removeItem(@NonNull T t) {
        return removeItem(getIndex(t));
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


    /**
     * 初始化item
     *
     * @param holder
     * @param item
     * @param index  相对于List的位置
     */
    public abstract void onBindHolder(XXFViewHolder<V, T> holder, @Nullable T item, int index);

    @Override
    public final XXFViewHolder<V, T> onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        V v = onCreateBinding(LayoutInflater.from(viewGroup.getContext()), viewGroup, viewType);
        return onCreateItemHolder(v, viewGroup, viewType);
    }

    /**
     * 创建item viewHolder
     *
     * @return
     */
    protected XXFViewHolder<V, T> onCreateItemHolder(V v, ViewGroup viewGroup, int viewType) {
        XXFViewHolder<V, T> viewHolder = new XXFViewHolder(this, v.getRoot(), true);
        viewHolder.setBinding(v);
        return viewHolder;
    }


    @Override
    public final void onBindViewHolder(XXFViewHolder<V, T> holder, int position) {
        onBindHolder(holder, getItem(position), position);
    }


    protected OnItemClickListener<V, T> onItemClickListener;
    protected OnItemLongClickListener<V, T> onItemLongClickListener;
    protected OnItemChildClickListener<V, T> onItemChildClickListener;
    protected OnItemChildLongClickListener<V, T> onItemChildLongClickListener;

    /**
     * 设置Item点击,you must call holder.bindXXclick()
     *
     * @param l
     */
    public void setOnItemClickListener(OnItemClickListener<V, T> l) {
        this.onItemClickListener = l;
    }

    /**
     * 设置Item长按,you must call holder.bindXXclick()
     *
     * @param l
     */
    public void setOnItemLongClickListener(OnItemLongClickListener<V, T> l) {
        this.onItemLongClickListener = l;
    }

    /**
     * 设置Item child长按,you must call holder.bindXXclick()
     *
     * @param l
     */
    public void setOnItemChildLongClickListener(OnItemChildLongClickListener<V, T> l) {
        this.onItemChildLongClickListener = l;
    }

    /**
     * 设置Item child点击,you must call holder.bindXXclick()
     *
     * @param l
     */
    public void setOnItemChildClickListener(OnItemChildClickListener<V, T> l) {
        this.onItemChildClickListener = l;
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        this.attachedRecyclerView = recyclerView;
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        this.attachedRecyclerView = null;
        super.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public void onViewRecycled(@NonNull XXFViewHolder<V, T> holder) {
        super.onViewRecycled(holder);
        holder.onViewRecycled();
    }

    @Override
    public void onViewAttachedToWindow(@NonNull XXFViewHolder<V, T> holder) {
        super.onViewAttachedToWindow(holder);
        holder.onViewAttachedToWindow();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull XXFViewHolder<V, T> holder) {
        super.onViewDetachedFromWindow(holder);
        holder.onViewDetachedFromWindow();
    }

    /**
     * 直到adapter attatch到recyclerView 上才有 也就是先执行setAdapter
     *
     * @return
     */
    @CheckResult
    @Nullable
    public RecyclerView getRecyclerView() {
        return attachedRecyclerView;
    }

    /**
     * 获取上下文对象
     *
     * @return
     */
    @Nullable
    public Context getContext() {
        return attachedRecyclerView != null ? attachedRecyclerView.getContext() : null;
    }
}

