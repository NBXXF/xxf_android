package com.xxf.view.recyclerview.itemdecorations;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
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
    private boolean showLastDivider;
    private final Rect mBounds = new Rect();


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

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        //super.onDraw(c,parent,state);
        if (parent.getLayoutManager() == null || getDrawable() == null) {
            return;
        }
        if (mOrientation == VERTICAL) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    private void drawVertical(Canvas canvas, RecyclerView parent) {
        canvas.save();
        final int left;
        final int right;
        //noinspection AndroidLintNewApi - NewApi lint fails to handle overrides.
        if (parent.getClipToPadding()) {
            left = parent.getPaddingLeft();
            right = parent.getWidth() - parent.getPaddingRight();
            canvas.clipRect(left, parent.getPaddingTop(), right,
                    parent.getHeight() - parent.getPaddingBottom());
        } else {
            left = 0;
            right = parent.getWidth();
        }

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            parent.getDecoratedBoundsWithMargins(child, mBounds);
            final int bottom = mBounds.bottom + Math.round(child.getTranslationY());

            Drawable mDivider=getDrawable();
            final RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom()+lp.bottomMargin;
            //最后一条可能隐藏
            if(top<bottom) {
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(canvas);
            }
        }
        canvas.restore();
    }

    private void drawHorizontal(Canvas canvas, RecyclerView parent) {
        canvas.save();
        final int top;
        final int bottom;
        //noinspection AndroidLintNewApi - NewApi lint fails to handle overrides.
        if (parent.getClipToPadding()) {
            top = parent.getPaddingTop();
            bottom = parent.getHeight() - parent.getPaddingBottom();
            canvas.clipRect(parent.getPaddingLeft(), top,
                    parent.getWidth() - parent.getPaddingRight(), bottom);
        } else {
            top = 0;
            bottom = parent.getHeight();
        }

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            parent.getLayoutManager().getDecoratedBoundsWithMargins(child, mBounds);
            //原始代码
//            final int right = mBounds.right + Math.round(child.getTranslationX());
//            final int left = right - mDivider.getIntrinsicWidth();
//            mDivider.setBounds(left, top, right, bottom);
//            mDivider.draw(canvas);

            //grid为了列均分 所以拆分成左右各不均等的部分
            Drawable mDivider=getDrawable();
            final RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int rightPartLeft = child.getRight()+lp.rightMargin+ Math.round(child.getTranslationX());
            final int rightPartRight = mBounds.right + Math.round(child.getTranslationX());
            if(rightPartRight>rightPartLeft) {
                mDivider.setBounds(rightPartLeft, top, rightPartRight, bottom);
                mDivider.draw(canvas);
            }
            final int leftPartLeft = mBounds.left+ Math.round(child.getTranslationX());
            final int leftPartRight = child.getLeft()-lp.leftMargin+ Math.round(child.getTranslationX());
            if(leftPartRight>leftPartLeft) {
                mDivider.setBounds(leftPartLeft, top, leftPartRight, bottom);
                mDivider.draw(canvas);
            }

        }
        canvas.restore();
    }

    @CallSuper
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //空 不处理
        if (getDrawable() == null) {
            outRect.set(0, 0, 0, 0);
        } else if (parent.getLayoutManager() instanceof GridLayoutManager && mOrientation == HORIZONTAL) {
            GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
            GridLayoutManager.LayoutParams lp = (GridLayoutManager.LayoutParams) view.getLayoutParams();
            int spanCount = layoutManager.getSpanCount();
            //占满一行
            if(lp.getSpanSize()==spanCount){
                outRect.set(0, 0, 0, 0);
            }else {
                int spanIndex = lp.getSpanIndex();
                int column = spanIndex;
                int mSpacing = Math.max(getDrawable().getIntrinsicHeight(), getDrawable().getIntrinsicWidth());

                //均分策略算法 其他算法有bug
                //列 竖向划线
                int left = column * mSpacing / spanCount;
                int right = mSpacing - (column + 1) * mSpacing / spanCount;

                outRect.set(left, 0, right, 0);
            }
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
