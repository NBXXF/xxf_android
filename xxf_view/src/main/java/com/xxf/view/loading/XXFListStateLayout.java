package com.xxf.view.loading;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;

import androidx.recyclerview.widget.RecyclerView;

/**
 * 子组件包括RecyclerView,自动处理空状态,其他状态业务控制
 */
public class XXFListStateLayout extends XXFStateLayout {
    private RecyclerView childRecyclerView;

    public XXFListStateLayout(Context context) {
        super(context);
    }

    public XXFListStateLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public XXFListStateLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            //找到第一个就行了
            if (childAt instanceof RecyclerView) {

                childRecyclerView = (RecyclerView) childAt;
                childRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (childRecyclerView.getAdapter() == null) {
                            return;
                        }
                        Log.d("=====>", "3:" + childRecyclerView.getAdapter().getItemCount());
                        setViewState(childRecyclerView.getAdapter().getItemCount() <= 0 ? ViewState.VIEW_STATE_EMPTY : ViewState.VIEW_STATE_CONTENT);

                    }
                });
                return;
            }
            if (childAt instanceof AbsListView) {
                AbsListView absListView = (AbsListView) childAt;
                absListView.addOnLayoutChangeListener(new OnLayoutChangeListener() {
                    @Override
                    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                        if (absListView.getAdapter() == null) {
                            return;
                        }
                        Log.d("=====>", "4:" + absListView.getAdapter().getCount());
                        setViewState(absListView.getAdapter().getCount() <= 0 ? ViewState.VIEW_STATE_EMPTY : ViewState.VIEW_STATE_CONTENT);
                    }
                });
            }
        }
    }

}
