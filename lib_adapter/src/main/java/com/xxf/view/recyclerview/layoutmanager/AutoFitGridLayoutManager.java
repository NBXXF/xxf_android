package com.xxf.view.recyclerview.layoutmanager;

import android.content.Context;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xxf.utils.ScreenUtils;

/**
 * 格子布局 手机 pad 横竖自适应
 */
public class AutoFitGridLayoutManager extends GridLayoutManager {
    public int expectedColumnWidth;

    /**
     * @param context
     * @param expectedColumnWidth 像素 期望格子宽度 像素
     */
    public AutoFitGridLayoutManager(Context context, int expectedColumnWidth) {
        super(context, 1);
        this.expectedColumnWidth = expectedColumnWidth;
    }

    /**
     * @param expectedColumnWidth 像素 期望格子宽度 像素
     */
    public void setExpectedColumnWidth(int expectedColumnWidth) {
        if (this.expectedColumnWidth != expectedColumnWidth) {
            this.expectedColumnWidth = expectedColumnWidth;
            autoFitSpan();
        }
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        autoFitSpan();
        super.onLayoutChildren(recycler, state);
    }

    /**
     * 自动适配span 的数量
     */
    public void autoFitSpan() {
        int totalSpace = 0;
        if (getOrientation() == VERTICAL) {
            totalSpace = getWidth() - getPaddingStart() - getPaddingEnd();
        } else {
            totalSpace = getWidth() - getPaddingTop() - getPaddingBottom();
        }
        if (totalSpace > 0) {
            int expectedColumn = ScreenUtils.spanCount(totalSpace, expectedColumnWidth);
            setSpanCount(expectedColumn);
        }
    }
}
