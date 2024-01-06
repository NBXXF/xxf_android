package com.xxf.view.recyclerview.layoutmanager;

import android.content.Context;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * 格子布局 手机 pad 横竖自适应
 * 自动计算格子数量
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
    public void onLayoutCompleted(RecyclerView.State state) {
        super.onLayoutCompleted(state);
        autoFitSpan();
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
            int expectedColumn = spanCount(totalSpace, expectedColumnWidth);
            setSpanCount(expectedColumn);
        }
    }

    /**
     * 格子布局
     *
     * @param parentWidth      父容器宽度
     * @param gridExpectedSize 最小格子宽度 px
     * @return
     */
    public int spanCount(int parentWidth, int gridExpectedSize) {
        int screenWidth = parentWidth;
        int spanCount = screenWidth / gridExpectedSize;
        if (spanCount <= 0) {
            spanCount = 1;
        }
        return spanCount;
    }

}
