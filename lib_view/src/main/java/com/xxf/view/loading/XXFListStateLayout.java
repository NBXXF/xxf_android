package com.xxf.view.loading;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.ListAdapter;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.InnerRecyclerViewPool;
import androidx.recyclerview.widget.RecyclerView;

import com.xxf.view.recyclerview.adapter.XXFUIAdapterObserver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 子组件包括RecyclerView,ListView,自动处理空状态,其他状态业务控制
 * 注意！！！！！
 * 业务层 想要控制adapter.registerAdapterDataObserver
 * 请在recyclerView.setAdapter(mAdapter)之后设置,以保证XXFListStateLayout有限处理
 */
public class XXFListStateLayout extends XXFStateLayout implements InnerRecyclerViewPool.OnAdapterChangedListener {

    public XXFListStateLayout(Context context) {
        super(context);
    }

    public XXFListStateLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public XXFListStateLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 是否宽容模式,只针对 XXFRecyclerAdapter,
     * 如果设置为true 则自动判断不包含header footer
     * 否则 判断包含header footer
     */
    @Deprecated
    private boolean lenient;

    /**
     * 设置是否 宽容模式,只针对 XXFRecyclerAdapter,
     *
     * @param lenient
     */
    @Deprecated
    public void setLenient(boolean lenient) {
        this.lenient = lenient;
    }

    @Deprecated
    public boolean isLenient() {
        return lenient;
    }

    RecyclerView childRecyclerView;
    AbsListView childAbsListView;
    private Map<Object, Object> cacheObservers = new HashMap<>();
    private final RecyclerView.AdapterDataObserver recyclerViewDataObserver = new XXFUIAdapterObserver() {
        @Override
        protected void updateUI() {
            if (childRecyclerView != null
                    && childRecyclerView.getAdapter() != null) {
                if (childRecyclerView.getAdapter() instanceof ConcatAdapter) {
                    ConcatAdapter concatAdapter = (ConcatAdapter) childRecyclerView.getAdapter();
                    List<? extends RecyclerView.Adapter<? extends RecyclerView.ViewHolder>> adapters = concatAdapter.getAdapters();
                    //获取最后一个子adapter
                    if (adapters != null && !adapters.isEmpty()) {
                        RecyclerView.Adapter<? extends RecyclerView.ViewHolder> lastAdapter = adapters.get(adapters.size() - 1);
                        handleViewState(lastAdapter);
                    } else {
                        handleViewState(childRecyclerView.getAdapter());
                    }
                } else {
                    handleViewState(childRecyclerView.getAdapter());
                }
            }
        }
    };


    protected void handleViewState(RecyclerView.Adapter<? extends RecyclerView.ViewHolder> adapter) {
        setViewState(adapter.getItemCount() <= 0 ? ViewState.VIEW_STATE_EMPTY : ViewState.VIEW_STATE_CONTENT);
    }

    private final DataSetObserver listViewDataObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            changeViewState();
        }

        private void changeViewState() {
            if (childAbsListView != null
                    && childAbsListView.getAdapter() != null) {
                setViewState(childAbsListView.getAdapter().getCount() <= 0 ? ViewState.VIEW_STATE_EMPTY : ViewState.VIEW_STATE_CONTENT);
            }
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
            changeViewState();
        }
    };

    InnerRecyclerViewPool listenRecycledViewPool;

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() == 0) {
            throw new RuntimeException("empty child!");
        }
        final View childAt1 = getChildAt(0);
        if (childAt1 instanceof RecyclerView) {
            childRecyclerView = (RecyclerView) childAt1;
            if (childRecyclerView.getRecycledViewPool() == null) {
                childRecyclerView.setRecycledViewPool(listenRecycledViewPool = new InnerRecyclerViewPool());
            } else if (childRecyclerView.getRecycledViewPool().getClass() == RecyclerView.RecycledViewPool.class) {
                childRecyclerView.setRecycledViewPool(listenRecycledViewPool = new InnerRecyclerViewPool());
            } else if (childRecyclerView.getRecycledViewPool() instanceof InnerRecyclerViewPool) {
                listenRecycledViewPool = (InnerRecyclerViewPool) childRecyclerView.getRecycledViewPool();
            } else {
                throw new RuntimeException("recycledViewPool must InnerRecyclerViewPool");
            }
            //提前感知设置adapter的时机
            listenRecycledViewPool.removeAdapterChangedListener(this);
            listenRecycledViewPool.addAdapterChangedListener(this);
        }
        childAt1.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (childAt1 instanceof AbsListView) {
                            childAbsListView = (AbsListView) childAt1;
                            ListAdapter adapter = childAbsListView.getAdapter();
                            if (adapter != null) {
                                if (cacheObservers.get(adapter) == null) {
                                    adapter.registerDataSetObserver(listViewDataObserver);
                                    cacheObservers.put(adapter, listViewDataObserver);
                                }
                            }
                        }
                    }
                });
    }

    @Override
    public void onAdapterChanged(@Nullable RecyclerView.Adapter<?> oldAdapter, @Nullable RecyclerView.Adapter<?> newAdapter, boolean compatibleWithPrevious) {
        if (oldAdapter != null) {
            try {
                oldAdapter.unregisterAdapterDataObserver(recyclerViewDataObserver);
            } catch (Throwable ignored) {
            }
        }
        if (newAdapter != null) {
            try {
                oldAdapter.registerAdapterDataObserver(recyclerViewDataObserver);
            } catch (Throwable ignored) {
            }
        }
    }
}
