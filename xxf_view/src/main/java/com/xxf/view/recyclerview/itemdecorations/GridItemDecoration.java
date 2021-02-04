package com.xxf.view.recyclerview.itemdecorations;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 格子 item分割线
 */
public class GridItemDecoration extends RecyclerView.ItemDecoration {

    // 分割线大小
    private int mSpacing;
    // 边沿是否存在
    private boolean mHasEdge;

    public GridItemDecoration(int spacing) {
        this(spacing, false);
    }

    public GridItemDecoration(int spacing, boolean hasEdge) {
        this.mSpacing = spacing;
        this.mHasEdge = hasEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (!(parent.getLayoutManager() instanceof GridLayoutManager)) {
            return;
        }
        int mSpanCount = ((GridLayoutManager) parent.getLayoutManager()).getSpanCount();
        int position = parent.getChildAdapterPosition(view);
        int column = position % mSpanCount;

        if (mHasEdge) {
            outRect.left = mSpacing - column * mSpacing / mSpanCount;
            outRect.right = (column + 1) * mSpacing / mSpanCount;
            if (position < mSpanCount) {
                outRect.top = mSpacing;
            }
            outRect.bottom = mSpacing;
        } else {
            outRect.left = column * mSpacing / mSpanCount;
            outRect.right = mSpacing - (column + 1) * mSpacing / mSpanCount;
            if (position >= mSpanCount) {
                outRect.top = mSpacing;
            }
        }
    }
}
