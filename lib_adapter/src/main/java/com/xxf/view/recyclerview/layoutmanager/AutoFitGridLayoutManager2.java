package com.xxf.view.recyclerview.layoutmanager;

import android.content.Context;
import android.util.Range;
import android.util.Size;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * 格子布局 手机 pad 横竖自适应
 * 自动计算格子数量
 */
public class AutoFitGridLayoutManager2 extends GridLayoutManager {
    static class Builder {
        Context context;
        /**
         * 期望的大小
         */
        public Size expectedSize;

        /**
         * 间距
         */
        public Size spacing = new Size(0, 0);

        /**
         * 列的区间
         */
        public Range<Integer> columnRange = new Range(1, Integer.MAX_VALUE);

        /**
         * 行的区间
         */
        public Range<Integer> rowRange = new Range(1, Integer.MAX_VALUE);

        public Builder(Context context, Size expectedSize) {
            this.context = context;
            this.expectedSize = expectedSize;
        }

        public Builder setExpectedSize(Size expectedSize) {
            this.expectedSize = expectedSize;
            return this;
        }

        public Builder setSpacing(Size spacing) {
            this.spacing = spacing;
            return this;
        }

        public Builder setColumnRange(Range<Integer> columnRange) {
            this.columnRange = columnRange;
            return this;
        }

        public Builder setRowRange(Range<Integer> rowRange) {
            this.rowRange = rowRange;
            return this;
        }

        public AutoFitGridLayoutManager2 build() {
            return new AutoFitGridLayoutManager2(context, new Config(expectedSize, spacing, columnRange, rowRange));
        }
    }


    public static class Config {
        /**
         * 期望的大小
         */
        public Size expectedSize;

        /**
         * 间距
         */
        public Size spacing;

        /**
         * 列的区间
         */
        public Range<Integer> columnRange;

        /**
         * 行的区间
         */
        public Range<Integer> rowRange;

        public Config(Size expectedSize, Size spacing, Range<Integer> columnRange, Range<Integer> rowRange) {
            this.expectedSize = expectedSize;
            this.spacing = spacing;
            this.columnRange = columnRange;
            this.rowRange = rowRange;
        }
    }

    static class GridCalculator {
        Size totalSize;

        Config config;

        public GridCalculator(Size totalSize, Config config) {
            this.totalSize = totalSize;
            this.config = config;
        }

        public int calculateByWidth() {
            return adaptSpanCount(totalSize.getWidth(), config.expectedSize.getWidth(), config.spacing.getWidth(), config.columnRange);
        }


        public int calculateByHeight() {
            return adaptSpanCount(totalSize.getHeight(), config.expectedSize.getHeight(), config.spacing.getHeight(), config.rowRange);
        }

        private int adaptSpanCount(int parentWidth, int expectedColumnWidth, int spacingWidth, Range<Integer> spanRange) {
            int spanCount = parentWidth / expectedColumnWidth;
            while (spanCount > 1 && spanCount * expectedColumnWidth + (spanCount - 1) * spacingWidth > parentWidth) {
                spanCount--;
            }
            return Math.min(Math.max(spanCount, spanRange.getLower()), spanRange.getUpper());
        }
    }


    private Config config;

    public AutoFitGridLayoutManager2(Context context, Config config) {
        super(context, 1);
        this.config = config;
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
        Size totalSize = new Size(getWidth() - getPaddingStart() - getPaddingEnd(),
                getHeight() - getPaddingTop() - getPaddingBottom());
        int span = 1;
        if (getOrientation() == VERTICAL) {
            span = new GridCalculator(totalSize, config).calculateByWidth();
        } else {
            span = new GridCalculator(totalSize, config).calculateByHeight();
        }
        setSpanCount(span);
    }

}
