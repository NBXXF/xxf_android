package com.xxf.view.recyclerview.layoutmanager;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 平缓滚动 LinearLayoutManager
 */
public class SmoothLinearLayoutManager extends LinearLayoutManager {

    private SmoothLinearScroller.SNAP snap;

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
        RecyclerView.SmoothScroller smoothScroller = new SmoothLinearScroller(recyclerView.getContext(), this.snap);
        smoothScroller.setTargetPosition(position);
        startSmoothScroll(smoothScroller);
    }
}
