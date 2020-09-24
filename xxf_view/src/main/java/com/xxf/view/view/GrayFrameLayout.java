package com.xxf.view.view;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @Description: 灰色模式
 * 如果用了WebView，请加上webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
 * @Author: XGod
 * @CreateDate: 2020/9/24 10:14
 */
public class GrayFrameLayout extends FrameLayout {
    private Paint mPaint = new Paint();
    ColorMatrix cm = new ColorMatrix();
    boolean isGrayColor;

    public GrayFrameLayout(@NonNull Context context) {
        super(context);
    }

    public GrayFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GrayFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public GrayFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * 是否是灰色模式
     *
     * @return
     */
    public boolean isGrayColor() {
        return isGrayColor;
    }

    /**
     * 设置是否灰色模式 否则默认颜色
     *
     * @param grayColor
     */
    public void setGrayColor(boolean grayColor) {
        this.isGrayColor = grayColor;
        if (this.isGrayColor) {
            cm.setSaturation(0);
            mPaint.setColorFilter(new ColorMatrixColorFilter(cm));
        } else {
            mPaint.setColorFilter(null);
        }
    }
}
