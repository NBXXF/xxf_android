package com.xxf.view.round;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Outline;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;

/**
 * @Description: view裁切工具类
 * @Author: XGod
 * @CreateDate: 2018/6/25 10:50
 */
public class CornerUtil {
    public static void clipViewCircle(View view) {
        view.setClipToOutline(true);
        view.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setOval(0, 0, view.getWidth(), view.getHeight());
            }
        });
    }

    public static void clipViewRoundRect(View view, final int radius) {
        view.setClipToOutline(true);
        view.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), radius);
            }
        });
    }

    public static void clipView(View view, AttributeSet attrs) {
        if (view != null && attrs != null) {
            TypedArray radiusTr = view.getContext().obtainStyledAttributes(attrs, R.styleable.xxf_radius_style);
            int radius = radiusTr.getDimensionPixelSize(R.styleable.xxf_radius_style_radius, 0);
            int dp360 = dip2px(view.getContext(), 360);
            if (radius >= dp360) {
                clipViewCircle(view);
            } else {
                clipViewRoundRect(view, radius);
            }
            radiusTr.recycle();
        }
    }

    public static void clipViewRadius(View view, float radius) {
        int dp360 = dip2px(view.getContext(), 360);
        if (radius >= dp360) {
            clipViewCircle(view);
        } else {
            clipViewRoundRect(view, (int) radius);
        }
    }

    private static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}