package com.xxf.view.recyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

public class AutoPollRecyclerView extends MaxHeightRecyclerView {
    private static final long TIME_AUTO_POLL = 3000;
    int loopIndex = 0;
    final Runnable autoPollTask = new Runnable() {
        @Override
        public void run() {
            Adapter adapter = getAdapter();
            if (adapter != null && adapter.getItemCount() > 0) {
                int index = (++loopIndex) % adapter.getItemCount();
                if (index == 0) {
                    scrollToPosition(index);
                } else {
                    smoothScrollToPosition(index);
                }
                postDelayed(autoPollTask, TIME_AUTO_POLL);
            }
        }
    };

    public AutoPollRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    public void start() {
        removeCallbacks(autoPollTask);
        setLayoutManager(new LinearLayoutManager(getContext(), HORIZONTAL, false) {
            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, State state, int position) {
//                super.smoothScrollToPosition(recyclerView, state, position);
                RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(recyclerView.getContext()) {

                    @Override
                    protected int calculateTimeForDeceleration(int dx) {
                        return super.calculateTimeForDeceleration(dx) * 3;
                    }

                    @Override
                    protected int calculateTimeForScrolling(int dx) {
                        return super.calculateTimeForScrolling(dx);
                    }
                };
                smoothScroller.setTargetPosition(position);
                startSmoothScroll(smoothScroller);
            }
        });
        if (getLayoutManager() instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) getLayoutManager();
            loopIndex = linearLayoutManager.findFirstVisibleItemPosition();
            if (loopIndex == NO_POSITION) {
                loopIndex = 0;
            }
        }
        postDelayed(autoPollTask, TIME_AUTO_POLL);
    }

    public void stop() {
        removeCallbacks(autoPollTask);
        loopIndex = 0;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                stop();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
                start();
                break;
        }
        return super.onTouchEvent(e);
    }
}