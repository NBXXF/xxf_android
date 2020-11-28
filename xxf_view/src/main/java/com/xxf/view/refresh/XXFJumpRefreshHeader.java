package com.xxf.view.refresh;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;

import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshKernel;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.constant.RefreshState;
import com.scwang.smart.refresh.layout.constant.SpinnerStyle;
import com.xxf.view.databinding.XxfJumpRefreshHeaderBinding;

/**
 * @Description: java类作用描述
 * @Author: XGod
 * @CreateDate: 2020/6/11 19:16
 */
public class XXFJumpRefreshHeader implements RefreshHeader {
    private SpinnerStyle mSpinnerStyle = SpinnerStyle.Translate;
    protected XxfJumpRefreshHeaderBinding refreshHeaderBinding;

    public XXFJumpRefreshHeader(Context context) {
        refreshHeaderBinding = XxfJumpRefreshHeaderBinding.inflate(LayoutInflater.from(context));
        refreshHeaderBinding.headerAnimationView.setAnimation("xxf_loading.json");
        refreshHeaderBinding.headerAnimationView.setRepeatCount(Integer.MAX_VALUE);
    }

    public XxfJumpRefreshHeaderBinding getRefreshHeaderBinding() {
        return refreshHeaderBinding;
    }

    @NonNull
    @Override
    public View getView() {
        return refreshHeaderBinding.getRoot();
    }

    public void setSpinnerStyle(SpinnerStyle mSpinnerStyle) {
        this.mSpinnerStyle = mSpinnerStyle;
    }

    @NonNull
    @Override
    public SpinnerStyle getSpinnerStyle() {
        return this.mSpinnerStyle;
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
            case PullDownToRefresh:
                refreshHeaderBinding.headerAnimationView.playAnimation();
                break;
            case RefreshFinish:
            case PullDownCanceled:
            case None:
                refreshHeaderBinding.headerAnimationView.pauseAnimation();
                break;
        }
    }
}
