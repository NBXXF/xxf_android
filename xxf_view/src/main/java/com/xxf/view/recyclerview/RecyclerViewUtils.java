package com.xxf.view.recyclerview;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.LruCache;
import android.util.SparseIntArray;
import android.view.View;

import androidx.annotation.CheckResult;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

/**
 * @Description: RecyclerViewView 工具类
 * @Author: XGod
 * @CreateDate: 2020/7/30 13:57
 */
public class RecyclerViewUtils {


    /**
     * 清除动画
     *
     * @param recyclerView
     */
    public static void clearItemAnimator(@Nullable RecyclerView recyclerView) {
        if (recyclerView == null) {
            return;
        }
        if (recyclerView.getItemAnimator() != null) {
            recyclerView.getItemAnimator().setAddDuration(0);
            recyclerView.getItemAnimator().setChangeDuration(0);
            recyclerView.getItemAnimator().setMoveDuration(0);
            recyclerView.getItemAnimator().setRemoveDuration(0);
        }
        if (recyclerView.getItemAnimator() instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        }
    }

    /**
     * 截取屏幕 可见的部分
     * 如果用recyclerView直接截图会 最上最下的Item展示不全的问题
     *
     * @param recyclerView
     * @return
     */
    @Nullable
    @CheckResult
    public static Bitmap shotRecyclerViewVisibleItems(@NonNull RecyclerView recyclerView) {
        if (!checkShotRecyclerView(recyclerView)) {
            return null;
        }
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
        int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
        return shotRecyclerView(recyclerView, firstVisibleItem, lastVisibleItem);
    }

    /**
     * 是否支持截图
     *
     * @param recyclerView
     * @return
     */
    private static boolean checkShotRecyclerView(RecyclerView recyclerView) {
        return recyclerView != null
                && (recyclerView.getLayoutManager() instanceof LinearLayoutManager)
                && ((LinearLayoutManager) recyclerView.getLayoutManager()).getOrientation() == LinearLayoutManager.VERTICAL;
    }

    /**
     * 同步 截图RecyclerView
     * 如果有网络图片加载 请提前滚动一遍recyclerView
     * 有oom风险 不推荐
     *
     * @param recyclerView
     * @param startItem
     * @param endItem
     * @return
     */
    @Nullable
    @CheckResult
    public static Bitmap shotRecyclerView(@NonNull RecyclerView recyclerView, int startItem, int endItem) {
        if (!checkShotRecyclerView(recyclerView)) {
            return null;
        }
        try {
            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            Bitmap bigBitmap = null;
            if (adapter != null) {
                int size = endItem - startItem;
                int height = 0;
                Paint paint = new Paint();
                final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

                // Use 1/8th of the available memory for this memory cache.
                final int cacheSize = maxMemory / 8;
                LruCache<String, Bitmap> bitmapCache = new LruCache<>(cacheSize);
                SparseIntArray bitmapLeft = new SparseIntArray(size);
                SparseIntArray bitmapTop = new SparseIntArray(size);
                for (int i = 0; i < size; i++) {
                    int adapterPos = i + startItem;
                    RecyclerView.ViewHolder holder = adapter.createViewHolder(recyclerView, adapter.getItemViewType(adapterPos));
                    adapter.onBindViewHolder(holder, adapterPos);
                    RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
                    holder.itemView.measure(
                            View.MeasureSpec.makeMeasureSpec(recyclerView.getWidth() - layoutParams.leftMargin - layoutParams.rightMargin, View.MeasureSpec.EXACTLY),
                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                    );
                    holder.itemView.layout(
                            layoutParams.leftMargin,
                            layoutParams.topMargin,
                            holder.itemView.getMeasuredWidth() + layoutParams.leftMargin,
                            holder.itemView.getMeasuredHeight() + layoutParams.topMargin
                    );
                    holder.itemView.setDrawingCacheEnabled(true);
                    holder.itemView.buildDrawingCache();
                    Bitmap drawingCache = holder.itemView.getDrawingCache();
                    if (drawingCache != null) {
                        bitmapCache.put(String.valueOf(i), drawingCache);
                    }

                    height += layoutParams.topMargin;
                    bitmapLeft.put(i, layoutParams.leftMargin);
                    bitmapTop.put(i, height);
                    height += holder.itemView.getMeasuredHeight() + layoutParams.bottomMargin;
                }

                bigBitmap = Bitmap.createBitmap(recyclerView.getMeasuredWidth(), height, Bitmap.Config.ARGB_8888);
                Canvas bigCanvas = new Canvas(bigBitmap);
                Drawable lBackground = recyclerView.getBackground();
                if (lBackground instanceof ColorDrawable) {
                    ColorDrawable lColorDrawable = (ColorDrawable) lBackground;
                    int lColor = lColorDrawable.getColor();
                    bigCanvas.drawColor(lColor);
                }
                int paddingStart = ViewCompat.getPaddingStart(recyclerView);
                for (int i = 0; i < size; i++) {
                    Bitmap bitmap = bitmapCache.get(String.valueOf(i));
                    bigCanvas.drawBitmap(bitmap, paddingStart + bitmapLeft.get(i), bitmapTop.get(i), paint);
                    bitmap.recycle();
                }
            }
            return bigBitmap;
        } catch (OutOfMemoryError oom) {
            return null;
        }
    }

}
