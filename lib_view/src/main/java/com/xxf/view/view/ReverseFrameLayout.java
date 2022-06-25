package com.xxf.view.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.xxf.view.round.XXFRoundLayout;

/**
 * @Description: 反转颜色
 *  如果用了WebView，请加上webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/9/23 20:19
 */
public class ReverseFrameLayout extends XXFRoundLayout {
    private Paint mPaint = new Paint();
    float[] reverseColorMatrix = new float[]{
            -1, 0, 0, 1, 1,
            0, -1, 0, 1, 1,
            0, 0, -1, 1, 1,
            0, 0, 0, 1, 0
    };

    boolean reverseColor;

    public ReverseFrameLayout(@NonNull Context context) {
        super(context);
    }

    public ReverseFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ReverseFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//    public ReverseFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }

    public void toggleColor() {
        setReverseColor(!this.reverseColor);
    }

    /**
     * 设置是否反转颜色,否则是默认颜色
     *
     * @param reverseColor
     */
    public void setReverseColor(boolean reverseColor) {
        this.reverseColor = reverseColor;
        if (this.reverseColor) {
            mPaint.setColorFilter(new ColorMatrixColorFilter(reverseColorMatrix));
        } else {
            mPaint.setColorFilter(null);
        }
        this.invalidate();
    }

    /**
     * 是否是反转颜色
     *
     * @return
     */
    public boolean isReverseColor() {
        return reverseColor;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.saveLayer(null, mPaint, Canvas.ALL_SAVE_FLAG);
        super.dispatchDraw(canvas);
        canvas.restore();
    }


    @Override
    public void draw(Canvas canvas) {
        canvas.saveLayer(null, mPaint, Canvas.ALL_SAVE_FLAG);
        super.draw(canvas);
        canvas.restore();
    }

}