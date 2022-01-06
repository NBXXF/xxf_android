package com.xxf.view.recyclerview.adapter;

import android.view.View;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import java.util.Collection;
import java.util.List;

/**
 * 添加header footer 参考
 * 实例如下:
 * * ConcatAdapter totalAdapter = new ConcatAdapter();
 * * totalAdapter.addAdapter(new XXFViewAdapter(new TextView(null)))
 * * totalAdapter.addAdapter(new XXFViewAdapter(new TextView(null)))
 * * totalAdapter.addAdapter(new XXFViewAdapter(new TextView(null)))
 *
 * @param <V>
 * @param <T>
 */
public interface BaseAdapter<V extends ViewBinding, T> {

    List<T> getData();

    List<T> getCurrentList();

    int getDataSize();

    boolean isDataEmpty();

    boolean bindData(boolean isRefresh, @NonNull List<T> datas);

    boolean bindData(boolean isRefresh, @NonNull List<T> datas, @Nullable final Runnable commitCallback);

    void clearData();

    T getItem(@IntRange(from = 0) int index);

    int getIndex(@NonNull T t);

    boolean addItem(@IntRange(from = 0) int index, @NonNull T t);

    boolean addItems(@IntRange(from = 0) int index, @NonNull List<? extends T> datas);

    boolean addItems(@NonNull Collection<? extends T> datas);

    boolean addItem(@NonNull T t);

    boolean updateItem(@NonNull T t);

    boolean updateItem(int index, @NonNull T t);

    boolean removeItem(@IntRange(from = 0) int index);

    boolean removeItem(@NonNull T t);

    /**
     * 设置Item点击,you must call holder.bindXXclick()
     *
     * @param l
     */
    void setOnItemClickListener(OnItemClickListener<V, T> l);

    /**
     * 设置Item长按,you must call holder.bindXXclick()
     *
     * @param l
     */
    void setOnItemLongClickListener(OnItemLongClickListener<V, T> l);

    /**
     * 设置Item child长按,you must call holder.bindXXclick()
     *
     * @param l
     */
    void setOnItemChildLongClickListener(OnItemChildLongClickListener<V, T> l);

    /**
     * 设置Item child点击,you must call holder.bindXXclick()
     *
     * @param l
     */
    void setOnItemChildClickListener(OnItemChildClickListener<V, T> l);


    void callOnItemClick(XXFViewHolder<V, T> holder, View itemView, int index);

    boolean callOnItemLongClick(XXFViewHolder<V, T> holder, View itemView, int index);

    boolean callOnItemChildLongClick(XXFViewHolder<V, T> holder, View childView, int index);

    void callOnItemChildClick(XXFViewHolder<V, T> holder, View childView, int index);


    public RecyclerView getRecyclerView();

}
