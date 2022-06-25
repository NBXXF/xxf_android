package com.xxf.view.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.xxf.view.R;
import com.xxf.view.round.XXFRoundLayout;

/**
 * @Description: 等比例缩放子组件
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/11/19 19:33
 */
public class ScaleFrameLayout extends XXFRoundLayout {
    public ScaleFrameLayout(@NonNull Context context) {
        super(context);
    }

    public ScaleFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ScaleFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//    public ScaleFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.ScaleLayout);
        if (a.hasValue(R.styleable.ScaleLayout_scaleRatio)) {
            scaleRatio = a.getFloat(R.styleable.ScaleLayout_scaleRatio, 1.0f);
        }
        a.recycle();
    }

    private float scaleRatio = 1.0f;

    private void scaleViewGroup(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            ViewGroup.LayoutParams layoutParams = child.getLayoutParams();
            if (layoutParams instanceof MarginLayoutParams) {
                MarginLayoutParams marginParams = ((MarginLayoutParams) layoutParams);
                marginParams.setMargins(
                        (int) (marginParams.leftMargin * scaleRatio),
                        (int) (marginParams.topMargin * scaleRatio),
                        (int) (marginParams.rightMargin * scaleRatio),
                        (int) (marginParams.bottomMargin * scaleRatio));
            }
            child.setPadding((int) (child.getPaddingLeft() * scaleRatio),
                    (int) (child.getPaddingTop() * scaleRatio),
                    (int) (child.getPaddingRight() * scaleRatio),
                    (int) (child.getPaddingBottom() * scaleRatio));
            if (layoutParams.width > 0) {
                layoutParams.width = (int) (layoutParams.width * scaleRatio);
            }
            if (layoutParams.height > 0) {
                layoutParams.height = (int) (layoutParams.height * scaleRatio);
            }
            child.setLayoutParams(layoutParams);

            if (child instanceof ViewGroup) {
                scaleViewGroup((ViewGroup) child);
            } else if (child instanceof TextView) {
                TextView textView = (TextView) child;
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textView.getTextSize() * scaleRatio);
            }
        }
    }

    boolean scaled;

    @CallSuper
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!scaled && scaleRatio > 0) {
            scaleViewGroup(this);
            scaled = true;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}
