package com.xxf.view.titlebar;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.xxf.view.titlebar.initializer.BaseBarInitializer;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080
 * Date: 1/27/21 5:18 PM
 * Description:自动判断 是否可以执行点击
 */
public class AutoEnableTextView extends AppCompatTextView {
    public AutoEnableTextView(Context context) {
        super(context);
    }

    public AutoEnableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoEnableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        setEnabled(BaseBarInitializer.checkContainContent(this));
    }

    @Override
    public void setCompoundDrawables(@Nullable Drawable left, @Nullable Drawable top, @Nullable Drawable right, @Nullable Drawable bottom) {
        super.setCompoundDrawables(left, top, right, bottom);
        setEnabled(BaseBarInitializer.checkContainContent(this));
    }

    @Override
    public void setCompoundDrawablesRelative(@Nullable Drawable start, @Nullable Drawable top, @Nullable Drawable end, @Nullable Drawable bottom) {
        super.setCompoundDrawablesRelative(start, top, end, bottom);
        setEnabled(BaseBarInitializer.checkContainContent(this));
    }
}
