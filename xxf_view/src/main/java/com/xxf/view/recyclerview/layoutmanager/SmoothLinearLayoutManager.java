package com.xxf.view.recyclerview.layoutmanager;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 平缓滚动 LinearLayoutManager
 */
public class SmoothLinearLayoutManager extends LinearLayoutManager {

    public interface OnSmoothScrollListener {
        void onStart();

        void onStop();
    }

    private SmoothLinearScroller.SNAP snap;
    private OnSmoothScrollListener onSmoothScrollListener;

    public SmoothLinearLayoutManager setOnSmoothScrollListener(OnSmoothScrollListener onSmoothScrollListener) {
        this.onSmoothScrollListener = onSmoothScrollListener;
        return this;
    }

    public SmoothLinearLayoutManager setSnap(SmoothLinearScroller.SNAP snap) {
        this.snap = snap;
        return this;
    }

    public SmoothLinearLayoutManager(Context context, SmoothLinearScroller.SNAP snap) {
        super(context);
        this.snap = snap;
    }

    public SmoothLinearLayoutManager(Context context, int orientation, boolean reverseLayout, SmoothLinearScroller.SNAP snap) {
        super(context, orientation, reverseLayout);
        this.snap = snap;
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        RecyclerView.SmoothScroller smoothScroller = new SmoothLinearScroller(recyclerView.getContext(), this.snap) {
            @Override
            protected void onStart() {
                super.onStart();
                if (onSmoothScrollListener != null) {
                    onSmoothScrollListener.onStart();
                }
            }

            @Override
            protected void onStop() {
                super.onStop();
                if (onSmoothScrollListener != null) {
                    onSmoothScrollListener.onStop();
                }
            }
        };
        smoothScroller.setTargetPosition(position);
        startSmoothScroll(smoothScroller);
    }

}
