package com.xxf.view.titlebar.initializer;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import com.xxf.view.R;
import com.xxf.view.titlebar.SelectorDrawable;


public class LightBarInitializer extends BaseBarInitializer {

    @Override
    public TextView getLeftView(Context context) {
        TextView leftView = super.getLeftView(context);
        leftView.setTextColor(0xFF666666);
        setViewBackground(leftView, new SelectorDrawable.Builder()
                .setDefault(new ColorDrawable(0x00000000))
                .setFocused(new ColorDrawable(0x0C000000))
                .setPressed(new ColorDrawable(0x0C000000))
                .build());
        return leftView;
    }

    @Override
    public TextView getTitleView(Context context) {
        TextView titleView = super.getTitleView(context);
        titleView.setTextColor(0xFF222222);
        return titleView;
    }

    @Override
    public TextView getRightView(Context context) {
        TextView rightView = super.getRightView(context);
        rightView.setTextColor(0xFFA4A4A4);
        setViewBackground(rightView, new SelectorDrawable.Builder()
                .setDefault(new ColorDrawable(0x00000000))
                .setFocused(new ColorDrawable(0x0C000000))
                .setPressed(new ColorDrawable(0x0C000000))
                .build());
        return rightView;
    }

    @Override
    public View getLineView(Context context) {
        View lineView = super.getLineView(context);
        setViewBackground(lineView, new ColorDrawable(0xFFECECEC));
        return lineView;
    }

    @Override
    public Drawable getBackIcon(Context context) {
        return getDrawableResources(context, R.drawable.bar_arrows_left_black);
    }

    @Override
    public Drawable getBackgroundDrawable(Context context) {
        return new ColorDrawable(0xFFFFFFFF);
    }
}