package com.xxf.view.recyclerview;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.LruCache;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: RecyclerViewView 工具类
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
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

    private static Bitmap createBitmap(View v) {
        Bitmap bmp = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);
        v.draw(c);
        return bmp;
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
        return shotRecyclerViewVisibleItems(recyclerView, -1);
    }

    /**
     * 截取屏幕 可见的部分
     *
     * @param recyclerView
     * @param backgroundColor -1 代表不设置背景
     * @return
     */
    @Nullable
    @CheckResult
    public static Bitmap shotRecyclerViewVisibleItems(@NonNull RecyclerView recyclerView, int backgroundColor) {
        if (!checkShotRecyclerView(recyclerView)) {
            return null;
        }
        try {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            Paint paint = new Paint();
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
            // Use 1/8th of the available memory for this memory cache.
            final int cacheSize = maxMemory / 8;
            LruCache<Integer, Bitmap> bitmapCache = new LruCache<>(cacheSize);
            int height = recyclerView.getPaddingTop();
            List<Integer> topIndex = new ArrayList<>();
            for (int i = 0; i < recyclerView.getChildCount(); i++) {
                View childAt = recyclerView.getChildAt(i);
                ViewGroup.LayoutParams layoutParams = childAt.getLayoutParams();
                int topMargin = 0, bottomMargin = 0;
                if (layoutParams instanceof RecyclerView.LayoutParams) {
                    RecyclerView.LayoutParams marginLayoutParams = (RecyclerView.LayoutParams) layoutParams;
                    topMargin = marginLayoutParams.topMargin + linearLayoutManager.getTopDecorationHeight(childAt);
                    bottomMargin = marginLayoutParams.bottomMargin + linearLayoutManager.getBottomDecorationHeight(childAt);
                }
                int topY = height + topMargin;
                topIndex.add(topY);
                Bitmap bitmapFromView = createBitmap(childAt);
                bitmapCache.put(i, bitmapFromView);
                height = topY + childAt.getHeight() + bottomMargin;
            }
            height += recyclerView.getPaddingBottom();
            Bitmap bigBitmap = Bitmap.createBitmap(recyclerView.getMeasuredWidth(), height, Bitmap.Config.ARGB_8888);
            Canvas bigCanvas = new Canvas(bigBitmap);
            Drawable lBackground = recyclerView.getBackground();
            if (backgroundColor != -1) {
                bigCanvas.drawColor(backgroundColor);
            } else if (lBackground instanceof ColorDrawable) {
                ColorDrawable lColorDrawable = (ColorDrawable) lBackground;
                int lColor = lColorDrawable.getColor();
                bigCanvas.drawColor(lColor);
            }
            for (int i = 0; i < bitmapCache.size(); i++) {
                Bitmap bitmap = bitmapCache.get(i);
                int x = (bigBitmap.getWidth() - bitmap.getWidth()) / 2;
                bigCanvas.drawBitmap(bitmap, x > 0 ? x : 0, topIndex.get(i), paint);
                bitmap.recycle();
            }
            return bigBitmap;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
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

}
