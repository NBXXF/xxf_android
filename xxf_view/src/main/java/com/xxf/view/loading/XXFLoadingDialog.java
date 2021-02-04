package com.xxf.view.loading;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.xxf.arch.dialog.XXFDialog;
import com.xxf.arch.widget.progresshud.ProgressHUD;
import com.xxf.view.R;
import com.xxf.view.databinding.XxfLoadingDialogBinding;

import io.reactivex.rxjava3.functions.BiConsumer;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080
 * Date: 2/2/21 6:03 PM
 * Description: 进度会话框
 */
public class XXFLoadingDialog extends XXFDialog implements ProgressHUD {

    public XXFLoadingDialog(@NonNull Context context, @Nullable BiConsumer dialogConsumer) {
        super(context, R.style.xxf_loading_dialog_style, dialogConsumer);
        binding = XxfLoadingDialogBinding.inflate(LayoutInflater.from(context));
    }

    protected XxfLoadingDialogBinding binding;
    private Runnable dismissTask = new Runnable() {
        @Override
        public void run() {
            if (isShowing()) {
                dismiss();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        Window window = getWindow();
        if (window != null) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.dimAmount = 0.1f;
            lp.gravity = Gravity.CENTER;
            window.setAttributes(lp);
        }
/*        LayoutTransition lt = new LayoutTransition();
        lt.enableTransitionType(LayoutTransition.APPEARING | LayoutTransition.DISAPPEARING);
        ((ViewGroup) binding.getRoot()).setLayoutTransition(lt);*/
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    @Override
    public void showLoadingDialog(@Nullable String notice) {
        updateText(notice);
        binding.loadingIv.setAnimation("xxf_hud_loading.json");
        binding.loadingIv.setRepeatCount(Integer.MAX_VALUE);
        binding.loadingIv.playAnimation();
        if (!this.isShowing()) {
            this.show();
        }
    }

    @Override
    public void dismissLoadingDialog() {
        this.dismiss();
    }

    @Override
    public void dismissLoadingDialogWithSuccess(String notice, long delayTime) {
        updateText(notice);
        binding.loadingIv.setAnimation("xxf_hud_success.json");
        binding.loadingIv.setRepeatCount(0);
        binding.loadingIv.playAnimation();
        binding.getRoot().removeCallbacks(dismissTask);
        binding.getRoot().postDelayed(dismissTask, delayTime < 0 ? 0 : delayTime);
    }

    @Override
    public void dismissLoadingDialogWithFail(String notice, long delayTime) {
        updateText(notice);
        binding.loadingIv.setAnimation("xxf_hud_fail.json");
        binding.loadingIv.setRepeatCount(0);
        binding.loadingIv.playAnimation();
        binding.getRoot().removeCallbacks(dismissTask);
        binding.getRoot().postDelayed(dismissTask, delayTime < 0 ? 0 : delayTime);
    }

    public void updateText(String notice) {
        binding.msgTv.setText(notice);
        binding.msgTv.setVisibility(TextUtils.isEmpty(notice) ? View.GONE : View.VISIBLE);
    }

    @Override
    public void dismiss() {
        binding.getRoot().removeCallbacks(dismissTask);
        binding.loadingIv.cancelAnimation();
        super.dismiss();
    }

    @Override
    public boolean isShowLoading() {
        return this.isShowing();
    }
}
