package com.xxf.view.refresh;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.scwang.smart.refresh.layout.api.RefreshFooter;
import com.scwang.smart.refresh.layout.api.RefreshKernel;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.constant.RefreshState;
import com.scwang.smart.refresh.layout.constant.SpinnerStyle;
import com.xxf.view.databinding.XxfJumpRefreshFooterBinding;

import java.util.Objects;

/**
 * @Description: footer 刷新动画默认 loading
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/6/11 19:24
 */
public class XXFJumpRefreshFooter implements RefreshFooter {
    private SpinnerStyle mSpinnerStyle = SpinnerStyle.Translate;
    protected XxfJumpRefreshFooterBinding refreshFooterBinding;

    public XXFJumpRefreshFooter(@NonNull Context context) {
        Context context1 = Objects.requireNonNull(context);
        refreshFooterBinding = XxfJumpRefreshFooterBinding.inflate(LayoutInflater.from(context));
        refreshFooterBinding.footerAnimationView.setAnimation("xxf_load_more.json");
        refreshFooterBinding.footerAnimationView.setRepeatCount(Integer.MAX_VALUE);
    }

    public XxfJumpRefreshFooterBinding getRefreshFooterBinding() {
        return refreshFooterBinding;
    }

    @Override
    public boolean setNoMoreData(boolean noMoreData) {
        return false;
    }

    @NonNull
    @Override
    public View getView() {
        return refreshFooterBinding.getRoot();
    }

    public void setSpinnerStyle(SpinnerStyle mSpinnerStyle) {
        this.mSpinnerStyle = mSpinnerStyle;
    }

    @NonNull
    @Override
    public SpinnerStyle getSpinnerStyle() {
        return mSpinnerStyle;
    }

    @Override
    public void setPrimaryColors(int... colors) {

    }

    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int maxDragHeight) {

    }

    @Override
    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {

    }

    @Override
    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {

    }

    @Override
    public void onStartAnimator(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
    }

    @Override
    public int onFinish(@NonNull RefreshLayout refreshLayout, boolean success) {
        return XXFRefreshConfig.DURATION_REFRESH;
    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
        switch (newState) {
            case PullUpToLoad:
                refreshFooterBinding.footerAnimationView.playAnimation();
                break;
            case LoadFinish:
            case LoadReleased:
            case None:
                refreshFooterBinding.footerAnimationView.pauseAnimation();
                break;
        }
    }
}
