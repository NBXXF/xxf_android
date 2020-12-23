package com.xxf.view.loading;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;

import com.airbnb.lottie.LottieAnimationView;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description loading..
 */
public class XXFLoadingView extends LottieAnimationView {


    public XXFLoadingView(Context context) {
        this(context, null);
    }

    public XXFLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XXFLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setAnimation("xxf_loading.json");
        setRepeatCount(Integer.MAX_VALUE);
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);

        if (visibility == VISIBLE) {
            playAnimation();
        } else {
            pauseAnimation();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        pauseAnimation();
    }
}
