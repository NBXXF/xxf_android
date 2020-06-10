package com.xxf.view.loading;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;

import androidx.recyclerview.widget.RecyclerView;

import com.xxf.view.recyclerview.adapter.XXFUIAdapterObserver;

/**
 * 子组件包括RecyclerView,自动处理空状态,其他状态业务控制
 */
public class XXFListStateLayout extends XXFStateLayout {

    public XXFListStateLayout(Context context) {
        super(context);
    }

    public XXFListStateLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public XXFListStateLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    RecyclerView childRecyclerView;
    AbsListView childAbsListView;
    private final RecyclerView.AdapterDataObserver adapterDataObserver = new XXFUIAdapterObserver() {
        @Override
        protected void updateUI() {
            if (childRecyclerView != null
                    && childRecyclerView.getAdapter() != null) {
                setViewState(childRecyclerView.getAdapter().getItemCount() <= 0 ? ViewState.VIEW_STATE_EMPTY : ViewState.VIEW_STATE_CONTENT);
            }
        }
    };

    private final DataSetObserver dataSetObserver = new DataSetObserver() {
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

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() == 0) {
            throw new RuntimeException("empty child!");
        }
        final View childAt1 = getChildAt(0);
        childAt1.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (childAt1 instanceof RecyclerView) {
                            childRecyclerView = (RecyclerView) childAt1;
                            if (childRecyclerView.getAdapter() != null) {
                                try {
                                    childRecyclerView.getAdapter().unregisterAdapterDataObserver(adapterDataObserver);
                                } catch (Exception e) {
                                }
                                childRecyclerView.getAdapter().registerAdapterDataObserver(adapterDataObserver);
                            }
                        } else if (childAt1 instanceof AbsListView) {
                            childAbsListView = (AbsListView) childAt1;
                            try {
                                childAbsListView.getAdapter().unregisterDataSetObserver(dataSetObserver);
                            } catch (Exception e) {
                            }
                            childAbsListView.getAdapter().registerDataSetObserver(dataSetObserver);
                        }
                    }
                });
    }

}
