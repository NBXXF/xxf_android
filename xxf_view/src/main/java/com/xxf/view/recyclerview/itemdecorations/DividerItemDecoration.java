package com.xxf.view.recyclerview.itemdecorations;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author xuanyouwu
 * @email xuanyouwu@163.com
 * @time 2016-08-24 14:04
 * <p>
 */
@Deprecated
public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    public static class Divider {

        @ColorInt
        public int color;
        public int size;
        public int marginStart;
        public int marginEnd;

        private Divider() {
        }

        public static class Builder {
            private int color;
            private int size;
            private int marginStart;
            private int marginEnd;

            public Builder color(int color) {
                this.color = color;
                return this;
            }

            public Builder size(int size) {
                this.size = size;
                return this;
            }

            public Builder margin(int marginStart, int marginEnd) {
                this.marginStart = marginStart;
                this.marginEnd = marginEnd;
                return this;
            }

            public Divider build() {
                Divider divider = new Divider();
                divider.size = this.size;
                divider.color = this.color;
                divider.marginStart = this.marginStart;
                divider.marginEnd = this.marginEnd;
                return divider;
            }

        }
    }

    private static final String TAG = "DividerItemDecoration";

    private Paint mPaint;
    private DividerLookup dividerLookup;

    public DividerItemDecoration() {
        mPaint = new Paint();
        dividerLookup = new DefaultDividerLookup();
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(child);
            Divider leftDivider = dividerLookup.getVerticalDivider(position);
            Divider bottomDivider = dividerLookup.getHorizontalDivider(position);
            if (leftDivider != null) drawLeftDivider(c, child, leftDivider);
            if (bottomDivider != null) drawBottomDivider(c, child, bottomDivider);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        int itemCount = state.getItemCount();
        int position = parent.getChildAdapterPosition(view);
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            int spanCount = gridLayoutManager.getSpanCount();
            GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();
            int spanIndex = spanSizeLookup.getSpanIndex(position, spanCount);
            int spanGroupIndex = spanSizeLookup.getSpanGroupIndex(position, spanCount);
            int lastSpanGroupIndex = spanSizeLookup.getSpanGroupIndex(itemCount - 1, spanCount);
            Divider vd = dividerLookup.getVerticalDivider(position);
            Divider hd = dividerLookup.getHorizontalDivider(position);
            int column = position % spanCount; // item column
            if (column == 0) {
                outRect.right = vd == null ? 0 : vd.size / 2;
            } else if (column == spanCount - 1) {
                outRect.left = vd == null ? 0 : vd.size / 2;
            } else {
                outRect.right = vd == null ? 0 : vd.size / 2;
                outRect.left = vd == null ? 0 : vd.size / 2;
            }
            outRect.bottom = hd == null ? 0 : hd.size;
            if (spanIndex == 0) outRect.left = 0;
            if (lastSpanGroupIndex == spanGroupIndex) outRect.bottom = 0;
            return;
        } else if (layoutManager instanceof LinearLayoutManager) {
            Divider hd = dividerLookup.getHorizontalDivider(position);
            outRect.bottom = hd == null ? 0 : hd.size;
            if (position == itemCount) outRect.bottom = 0;
            return;
        }
        throw new IllegalArgumentException("It is not currently supported StaggeredGridLayoutManager");
    }

    private void drawBottomDivider(Canvas c, View child, Divider bd) {
        mPaint.setColor(bd.color);
        c.drawRect(
                child.getLeft() + bd.marginStart,
                child.getBottom(),
                child.getRight() - bd.marginEnd,
                child.getBottom() + bd.size,
                mPaint
        );
    }

    private void drawLeftDivider(Canvas c, View child, Divider ld) {
        mPaint.setColor(ld.color);
        c.drawRect(
                child.getRight(),
                child.getTop() + ld.marginStart,
                child.getRight() + ld.size,
                child.getBottom() - ld.marginEnd,
                mPaint
        );
    }


    private class DefaultDividerLookup implements DividerLookup {

        private Divider mDivider;

        public DefaultDividerLookup() {
            mDivider = new Divider.Builder().color(Color.GRAY).size(2).build();
        }

        @Override
        public Divider getVerticalDivider(int position) {
            return mDivider;
        }

        @Override
        public Divider getHorizontalDivider(int position) {
            return mDivider;
        }
    }

    public void setDividerLookup(DividerLookup dividerLookup) {
        this.dividerLookup = dividerLookup;
    }

    public interface DividerLookup {
        Divider getVerticalDivider(int position);

        Divider getHorizontalDivider(int position);
    }

    public static class SimpleDividerLookup implements DividerLookup {

        @Override
        public Divider getVerticalDivider(int position) {
            return null;
        }

        @Override
        public Divider getHorizontalDivider(int position) {
            return null;
        }
    }

}
