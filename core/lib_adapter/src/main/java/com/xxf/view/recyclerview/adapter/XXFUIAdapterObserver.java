package com.xxf.view.recyclerview.adapter;


import androidx.annotation.CallSuper;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Description 监听adapter数据发生变化 不一定是网络填充 也可能是手动删除一条 数据已经发生变化 适合操作:展示空白页面提示
 * date createTime：2017/4/3
 * version 1.0.0
 */
public abstract class XXFUIAdapterObserver extends RecyclerView.AdapterDataObserver {

    @CallSuper
    @Override
    public void onChanged() {
        super.onChanged();
        updateUI();
    }

    @CallSuper
    @Override
    public void onItemRangeChanged(int positionStart, int itemCount) {
        super.onItemRangeChanged(positionStart, itemCount);
        updateUI();
    }

    @CallSuper
    @Override
    public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
        super.onItemRangeChanged(positionStart, itemCount, payload);
        updateUI();
    }

    @CallSuper
    @Override
    public void onItemRangeInserted(int positionStart, int itemCount) {
        super.onItemRangeInserted(positionStart, itemCount);
        updateUI();
    }

    @CallSuper
    @Override
    public void onItemRangeRemoved(int positionStart, int itemCount) {
        super.onItemRangeRemoved(positionStart, itemCount);
        updateUI();
    }

    @CallSuper
    @Override
    public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
        super.onItemRangeMoved(fromPosition, toPosition, itemCount);
        updateUI();
    }

    /**
     * 数据已经发生变化 适合操作:展示空白页面提示
     */
    protected abstract void updateUI();
}
