package com.xxf.view.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.xxf.view.round.XXFRoundLayout;

/**
 * @Description: 灰色模式
 * 如果用了WebView，请加上webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/9/24 10:14
 */
public class GrayFrameLayout extends XXFRoundLayout {
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


    @Override
    public void draw(Canvas canvas) {
        canvas.saveLayer(null, mPaint, Canvas.ALL_SAVE_FLAG);
        super.draw(canvas);
        canvas.restore();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.saveLayer(null, mPaint, Canvas.ALL_SAVE_FLAG);
        super.dispatchDraw(canvas);
        canvas.restore();
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
        if (this.isGrayColor != grayColor) {
            this.isGrayColor = grayColor;
            if (this.isGrayColor) {
                cm.setSaturation(0);
                mPaint.setColorFilter(new ColorMatrixColorFilter(cm));
            } else {
                mPaint.setColorFilter(null);
            }
            invalidate();
        }
    }
}
