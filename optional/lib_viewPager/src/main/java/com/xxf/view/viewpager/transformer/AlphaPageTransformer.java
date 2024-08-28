package com.xxf.view.viewpager.transformer;


import android.view.View;

import androidx.core.view.ViewCompat;


public class AlphaPageTransformer extends BasePageTransformer {
    private float mMinScale = 0.5f;

    public AlphaPageTransformer() {
    }

    public AlphaPageTransformer(float minScale) {
        setMinScale(minScale);
    }

    @Override
    public void handleInvisiblePage(View view, float position) {
        ViewCompat.setAlpha(view, 0);
    }

    @Override
    public void handleLeftPage(View view, float position) {
        view.setAlpha(mMinScale + (1 - mMinScale) * (1 + position));
    }

    @Override
    public void handleRightPage(View view, float position) {
        view.setAlpha(mMinScale + (1 - mMinScale) * (1 - position));
    }

    public void setMinScale(float minScale) {
        mMinScale = minScale;
    }
}