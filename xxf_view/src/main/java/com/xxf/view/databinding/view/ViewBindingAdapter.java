package com.xxf.view.databinding.view;

import androidx.databinding.BindingAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 扩展view的绑定
 */
public class ViewBindingAdapter extends androidx.databinding.adapters.ViewBindingAdapter {


    @BindingAdapter("android:layout_width")
    public static void setLayoutWidth(View view, int width) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params.width != width) {
            params.width = width;
            view.setLayoutParams(params);
        }
    }

    @BindingAdapter("android:layout_height")
    public static void setLayoutHeight(View view, int height) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params.height != height) {
            params.height = height;
            view.setLayoutParams(params);
        }
    }

}
