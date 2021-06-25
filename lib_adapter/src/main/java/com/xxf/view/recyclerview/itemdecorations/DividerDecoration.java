package com.xxf.view.recyclerview.itemdecorations;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;

import androidx.annotation.CallSuper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：6/25/19
 * Description ://分割线 只处理 横向和纵向 LinearLayoutManager
 * <p>
 * 1.扩展支持颜色
 * 2.内部支持动态判断方向
 */
public class DividerDecoration extends androidx.recyclerview.widget.DividerItemDecoration {

    /**
     * 默认会使用全局属性：@android:attr/listDivider
     *
     * @param context
     */
    public DividerDecoration(Context context) {
        super(context,VERTICAL);
    }

    /**
     * @param context
     * @param color   颜色
     * @param sizePx  分割线大小
     */
    public DividerDecoration(Context context, int color, int sizePx) {
        super(context, VERTICAL);
        Bitmap bitmap = Bitmap.createBitmap(sizePx, sizePx,
                Bitmap.Config.ARGB_8888);
        bitmap.eraseColor(color);
        this.setDrawable(new BitmapDrawable(context.getResources(), bitmap));
    }

    /**
     * 组件内部动态判断 不需要再设置了
     *
     * @param orientation
     */
    @Deprecated
    @Override
    public void setOrientation(int orientation) {
        super.setOrientation(orientation);
    }


    /**
     * 动态计算方向
     */
    protected void dynamicComputingDirection(RecyclerView parent) {
        if (parent.getLayoutManager() instanceof LinearLayoutManager) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
            super.setOrientation(layoutManager.getOrientation());
        }
    }

    @CallSuper
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        dynamicComputingDirection(parent);
        super.getItemOffsets(outRect, view, parent, state);
    }
}
