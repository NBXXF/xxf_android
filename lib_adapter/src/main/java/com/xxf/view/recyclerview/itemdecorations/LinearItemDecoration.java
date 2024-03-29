package com.xxf.view.recyclerview.itemdecorations;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.CallSuper;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：6/25/19
 * Description ://分割线 只处理 横向和纵向  如果是gridLayoutManager 需要添加两条不同方向的分割线
 * <p>
 * 1.扩展支持颜色
 * 2.兼容gridLayoutManager 需要添加两条不同方向的分割线
 */
public class LinearItemDecoration extends androidx.recyclerview.widget.DividerItemDecoration {
    public static class Builder {
        Context context;
        int orientation;
        Drawable drawable;
        /**
         * 是否展示最后一行或者最后一列的分割线
         * 默认false
         */
        private boolean showLastDivider;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }

        /**
         * orientation androidx.recyclerview.widget.DividerItemDecoration#HORIZONTAL androidx.recyclerview.widget.DividerItemDecoration#VERTICAL
         *
         * @param orientation
         * @return
         */
        public Builder setOrientation(int orientation) {
            this.orientation = orientation;
            return this;
        }

        public Builder horizontal() {
            this.orientation = HORIZONTAL;
            return this;
        }

        public Builder vertical() {
            this.orientation = VERTICAL;
            return this;
        }

        public Builder setDrawable(Drawable drawable) {
            this.drawable = drawable;
            return this;
        }

        public Builder setBitmap(Bitmap bitmap) {
            this.drawable = new BitmapDrawable(context.getResources(), bitmap);
            return this;
        }

        public Builder setColorDrawable(int color, int sizePx) {
            Bitmap bitmap = Bitmap.createBitmap(sizePx, sizePx,
                    Bitmap.Config.ARGB_8888);
            bitmap.eraseColor(color);
            this.drawable = new BitmapDrawable(context.getResources(), bitmap);
            return this;
        }

        public LinearItemDecoration build() {
            return new LinearItemDecoration(this);
        }
    }

    private int mOrientation;
    /**
     * 是否展示最后一行或者最后一列的分割线
     */
    private boolean showLastDivider;

    public LinearItemDecoration(Builder builder) {
        super(builder.context, builder.orientation);
        setOrientation(builder.orientation);
        this.setDrawable(builder.drawable);
        this.showLastDivider = builder.showLastDivider;
    }

    @Override
    public void setOrientation(int orientation) {
        this.mOrientation = orientation;
        super.setOrientation(orientation);
    }

    @CallSuper
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //空 不处理
        if (getDrawable() == null) {
            outRect.set(0, 0, 0, 0);
        } else if (parent.getLayoutManager() instanceof GridLayoutManager && mOrientation == HORIZONTAL) {
            //均分策略算法 其他算法有bug
            int mSpanCount = ((GridLayoutManager) parent.getLayoutManager()).getSpanCount();
            int position = parent.getChildAdapterPosition(view);
            int column = position % mSpanCount;
            int mSpacing = Math.max(getDrawable().getIntrinsicHeight(), getDrawable().getIntrinsicWidth());
            //列 竖向划线
            int left = column * mSpacing / mSpanCount;
            int right = mSpacing - (column + 1) * mSpacing / mSpanCount;

            outRect.set(left, 0, right, 0);
        } else {
            //控制 最后一条不画
            if (!showLastDivider) {
                int position = parent.getChildAdapterPosition(view);
                int itemCount = parent.getAdapter().getItemCount();
                int lastDividerOffset = getLastDividerOffset(parent);

                //控制 最后一条不画
                if (position >= itemCount - lastDividerOffset) {
                    // Don't set item offset for last line if mShowLastDivider = false
                    return;
                }
            }

            //父类 默认的 要画最后一条
            super.getItemOffsets(outRect, view, parent, state);
        }
    }

    /**
     * In the case mShowLastDivider = false,
     * Returns offset for how many views we don't have to draw a divider for,
     * for LinearLayoutManager it is as simple as not drawing the last child divider,
     * but for a GridLayoutManager it needs to take the span count for the last items into account
     * until we use the span count configured for the grid.
     *
     * @param parent RecyclerView
     * @return offset for how many views we don't have to draw a divider or 1 if its a
     * LinearLayoutManager
     */
    private int getLastDividerOffset(RecyclerView parent) {
        if (parent.getLayoutManager() instanceof GridLayoutManager) {
            GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
            GridLayoutManager.SpanSizeLookup spanSizeLookup = layoutManager.getSpanSizeLookup();
            int spanCount = layoutManager.getSpanCount();
            int itemCount = parent.getAdapter().getItemCount();
            for (int i = itemCount - 1; i >= 0; i--) {
                if (spanSizeLookup.getSpanIndex(i, spanCount) == 0) {
                    return itemCount - i;
                }
            }
        }

        return 1;
    }
}
