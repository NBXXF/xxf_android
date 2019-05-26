package com.xxf.view.recyclerview.layoutmanager;

import android.content.Context;
import android.support.v7.widget.LinearSmoothScroller;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 平滑滚动
 */
public class SmoothLinearScroller extends android.support.v7.widget.LinearSmoothScroller {

    public enum SNAP {
        /**
         * 顶部
         */
        SNAP_TO_START(LinearSmoothScroller.SNAP_TO_START),
        /**
         * 尾部
         */
        SNAP_TO_END(LinearSmoothScroller.SNAP_TO_END),
        /**
         * 默认
         */
        SNAP_TO_ANY(LinearSmoothScroller.SNAP_TO_ANY);
        int value;

        private SNAP(int value) {
            this.value = value;
        }
    }

    private SNAP snap;

    public SmoothLinearScroller(Context context, SNAP snap) {
        super(context);
        this.snap = snap;
    }

    @Override
    protected int getVerticalSnapPreference() {
        return this.snap.value;
    }

    @Override
    protected int getHorizontalSnapPreference() {
        return this.snap.value;
    }
}
