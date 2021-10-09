package com.xxf.view.recyclerview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.CheckResult;
import androidx.annotation.IntRange;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.xxf.view.recyclerview.SafeObservableArrayList;
import com.xxf.view.recyclerview.touchhelper.ItemTouchHelperAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * ClassName BaseRecyclerAdapter
 * Description index:相对于List集合的位置,position:表示在adapter中的位置
 * Company
 *
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2015/9/10 10:05
 * version
 */
public abstract class XXFRecyclerAdapter<V extends ViewBinding, T>
        extends RecyclerView.Adapter<XXFViewHolder<V, T>>
        implements ItemTouchHelperAdapter {
    public static final View inflaterView(@LayoutRes int id, RecyclerView recyclerView) {
        return LayoutInflater.from(recyclerView.getContext())
                .inflate(id, recyclerView, false);
    }

    /**
     * 【数据】发生改变的监听 不包括header/footer
     */
    protected final ObservableList.OnListChangedCallback<ObservableList<T>> dataChangeCallback = new ObservableList.OnListChangedCallback<ObservableList<T>>() {
        @Override
        public void onChanged(ObservableList<T> sender) {
            int start = getHeaderCount();
            notifyItemRangeChanged(start, getDataSize());
        }

        @Override
        public void onItemRangeChanged(ObservableList<T> sender, int positionStart, int itemCount) {
            int start = getHeaderCount() + positionStart;
            notifyItemRangeChanged(start, itemCount);
        }

        @Override
        public void onItemRangeInserted(ObservableList<T> sender, int positionStart, int itemCount) {
            int start = getHeaderCount() + positionStart;
            notifyItemRangeInserted(start, itemCount);
        }

        @Override
        public void onItemRangeMoved(ObservableList<T> sender, int fromPosition, int toPosition, int itemCount) {
            int start = getHeaderCount() + fromPosition;
            int to = getHeaderCount() + toPosition;
            if (itemCount == 1) {//刷新单个item
                notifyItemMoved(start, to);
            } else {// 全部数据刷新
                notifyItemRangeChanged(start, getDataSize());
            }
        }

        @Override
        public void onItemRangeRemoved(ObservableList<T> sender, int positionStart, int itemCount) {
            int start = getHeaderCount() + positionStart;
            notifyItemRangeRemoved(start, itemCount);
        }
    };

    private static final int HEADER_VIEW_TYPE = -10000;
    private static final int FOOTER_VIEW_TYPE = -20000;
    private final List<View> mHeaders = new ArrayList<View>();
    private final List<View> mFooters = new ArrayList<View>();
    private SafeObservableArrayList<T> dataList = new SafeObservableArrayList<T>();
    private RecyclerView attachedRecyclerView;

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

    public SafeObservableArrayList<T> getData() {
        return dataList;
    }

    public int getDataSize() {
        return getData().size();
    }

    public boolean isDataEmpty() {
        return getDataSize() <= 0;
    }

    public int getHeaderCount() {
        return mHeaders.size();
    }


    public int getFooterCount() {
        return mFooters.size();
    }

    public XXFRecyclerAdapter(@NonNull SafeObservableArrayList<T> data) {
        this.dataList = (data == null ? new SafeObservableArrayList<T>() : data);
        this.dataList.removeOnListChangedCallback(dataChangeCallback);
        this.dataList.addOnListChangedCallback(dataChangeCallback);
    }

    public XXFRecyclerAdapter() {
        this(new SafeObservableArrayList<T>());
    }

    @Nullable
    @CheckResult
    public View addHeader(@NonNull View view) {
        if (view == null) {
            throw new IllegalArgumentException("You can't have a null header!");
        }
        mHeaders.add(view);
        notifyDataSetChanged();
        return view;
    }

    @Nullable
    @CheckResult
    public View addHeader(@LayoutRes int id, @NonNull RecyclerView recyclerView) {
        return addHeader(inflaterView(id, recyclerView));
    }

    @Nullable
    @CheckResult
    public View addFooter(@NonNull View view) {
        if (view == null) {
            throw new IllegalArgumentException("You can't have a null footer!");
        }
        mFooters.add(view);
        notifyDataSetChanged();
        return view;
    }

    @Nullable
    @CheckResult
    public View addFooter(@LayoutRes int id, @NonNull RecyclerView recyclerView) {
        return addFooter(inflaterView(id, recyclerView));
    }

    @Nullable
    @CheckResult
    public View getHeader(int index) {
        return index < mHeaders.size() ? mHeaders.get(index) : null;
    }

    @Nullable
    @CheckResult
    public View getFooter(int index) {
        return index < mFooters.size() ? mFooters.get(index) : null;
    }

    public boolean isHeader(int viewType) {
        return viewType >= HEADER_VIEW_TYPE && viewType < (HEADER_VIEW_TYPE + mHeaders.size());
    }

    public boolean isHeader(View view) {
        return mHeaders.contains(view);
    }

    public boolean removeHeader(View view) {
        if (getHeaderCount() > 0) {
            if (mHeaders.contains(view)) {
                boolean remove = mHeaders.remove(view);
                notifyDataSetChanged();
                return remove;
            }
        }
        return false;
    }

    /**
     * 移除所有Header
     *
     * @return
     */
    public boolean removeHeaders() {
        if (mHeaders != null && !mHeaders.isEmpty()) {
            mHeaders.clear();
            notifyDataSetChanged();
            return true;
        }
        return false;
    }

    public boolean removeFooter(View view) {
        if (getFooterCount() > 0) {
            if (mFooters.contains(view)) {
                boolean remove = mFooters.remove(view);
                notifyDataSetChanged();
                return remove;
            }
        }
        return false;
    }

    /**
     * 移除所有footer
     *
     * @return
     */
    public boolean removeFooters() {
        if (mFooters != null && !mFooters.isEmpty()) {
            mFooters.clear();
            notifyDataSetChanged();
            return true;
        }
        return false;
    }

    public boolean isFooter(int viewType) {
        return viewType >= FOOTER_VIEW_TYPE && viewType < (FOOTER_VIEW_TYPE + mFooters.size());
    }

    public boolean isFooter(View view) {
        return mFooters.contains(view);
    }

    /**
     * @param isRefresh 是否下拉刷新
     * @param datas
     * @return
     */
    public boolean bindData(boolean isRefresh, @NonNull List<T> datas) {
        if (isRefresh) {//下拉刷新
            /**
             * 避免动画闪屏
             */
            getData().removeOnListChangedCallback(dataChangeCallback);
            getData().clear();
            notifyDataSetChanged();
            getData().addOnListChangedCallback(dataChangeCallback);
            return getData().addAll(datas);
        } else {
            //上拉加载 不能为空,并且不包含
            if (checkList(datas)
                    && !getData().containsAll(datas)) {
                getData().addAll(datas);
                return true;
            }
        }
        return false;
    }

    public void clearData() {
        if (!isDataEmpty()) {
            getData().clear();
        }
    }

    /**
     * 检查index是否有效
     *
     * @param index
     * @return true 有效 false 无效
     */
    private boolean checkIndex(@IntRange(from = 0) int index) {
        return index >= 0 && index < dataList.size();
    }

    /**
     * 检查add index是否有效
     *
     * @param addIndex
     * @return true 有效 false 无效
     */
    private boolean checkAddIndex(@IntRange(from = 0) int addIndex) {
        return addIndex >= 0 && addIndex <= dataList.size();
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
     * 检查对象是否有效
     *
     * @return true 不空
     */
    private boolean checkItem(T t) {
        return t != null;
    }

    /**
     * @param index 相对于List的位置
     * @return
     */
    public T getItem(@IntRange(from = 0) int index) {
        if (checkIndex(index)) {
            return getData().get(index);
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
        return getData().indexOf(t);
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
    public boolean addItem(@IntRange(from = 0) int index, @Nullable T t) {
        if (checkAddIndex(index)
                && checkItem(t)
                && !getData().contains(t)) {
            getData().add(index, t);
            return true;
        }
        return false;
    }

    public boolean addItems(@IntRange(from = 0) int index, @NonNull List<? extends T> datas) {
        if (checkList(datas)
                && checkAddIndex(index)
                && !getData().containsAll(datas)) {
            if (getData().addAll(index, datas)) {
                return true;
            }
        }
        return false;
    }

    public boolean addItems(@NonNull Collection<? extends T> datas) {
        if (checkList(datas)
                && !getData().containsAll(datas)) {
            if (getData().addAll(datas)) {
                return true;
            }
        }
        return false;
    }

    public boolean addItem(@NonNull T t) {
        if (checkItem(t)
                && !getData().contains(t)) {
            if (getData().add(t)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 更新item
     *
     * @param t
     * @return
     */
    public boolean updateItem(@NonNull T t) {
        if (checkItem(t)) {
            int index = getIndex(t);
            if (index >= 0) {
                getData().set(index, t);
                return true;
            }
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
        if (checkItem(t)) {
            if (index >= 0) {
                getData().set(index, t);
                return true;
            }
        }
        return false;
    }

    /**
     * @param index 相对于List的位置
     * @return
     */
    public boolean removeItem(@IntRange(from = 0) int index) {
        if (checkIndex(index)) {
            getData().remove(index);
            return true;
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
        if (isHeader(viewType)) {
            int whichHeader = Math.abs(viewType - HEADER_VIEW_TYPE);
            View headerView = mHeaders.get(whichHeader);
            return new XXFViewHolder(this, headerView, false);
        } else if (isFooter(viewType)) {
            int whichFooter = Math.abs(viewType - FOOTER_VIEW_TYPE);
            View footerView = mFooters.get(whichFooter);
            return new XXFViewHolder(this, footerView, false);
        } else {
            V v = onCreateBinding(LayoutInflater.from(viewGroup.getContext()), viewGroup, viewType);
            return onCreateItemHolder(v, viewGroup, viewType);
        }
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


    /**
     * 每条布局的type
     *
     * @param index 相对于List的位置
     * @return
     */
    public int getViewType(int index) {
        return 0;
    }

    @Override
    public final int getItemViewType(int position) {
        if (position < getHeaderCount()) {
            return HEADER_VIEW_TYPE + position;
        } else if (position < (getHeaderCount() + getData().size())) {
            return getViewType(position - getHeaderCount());
        } else {
            return FOOTER_VIEW_TYPE + position - getHeaderCount() - getData().size();
        }
    }

    @Override
    public int getItemCount() {
        return getHeaderCount() + getData().size() + getFooterCount();
    }

    @Override
    public final void onBindViewHolder(XXFViewHolder<V, T> holder, int position) {
        if (position >= getHeaderCount() && position < getHeaderCount() + getData().size()) {
            int index = position - getHeaderCount();
            onBindHolder(holder, getItem(index), index);
        }
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
        attachedRecyclerView = recyclerView;
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        attachedRecyclerView = null;
        super.onDetachedFromRecyclerView(recyclerView);
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

    /**
     * 滑动消失
     *
     * @param position The position of the item dismissed.
     */
    @Override
    public void onItemTouchDismiss(int position) {
        dataList.remove(position);
        //notifyItemRemoved(position);
    }

    /**
     * 拖拽交换
     *
     * @param fromPosition The start position of the moved item.
     * @param toPosition   Then resolved position of the moved item.
     * @return
     */
    @Override
    public boolean onItemTouchMove(int fromPosition, int toPosition) {
        int fromItemViewType = getItemViewType(fromPosition);
        int toItemViewType = getItemViewType(toPosition);
        if (isHeader(fromItemViewType) || isHeader(toItemViewType) ||
                isFooter(fromItemViewType) || isFooter(toItemViewType)) {
            return false;
        }
        boolean autoNotify = dataList.hasRegister(dataChangeCallback);
        if (autoNotify) {
            dataList.removeOnListChangedCallback(dataChangeCallback);
        }
        //fix issues
        try {
            Collections.swap(dataList, fromPosition - getHeaderCount(), toPosition - getHeaderCount());
            notifyItemMoved(fromPosition, toPosition);
        } finally {
            if (autoNotify) {
                dataList.addOnListChangedCallback(dataChangeCallback);
            }
        }
        return true;
    }
}

