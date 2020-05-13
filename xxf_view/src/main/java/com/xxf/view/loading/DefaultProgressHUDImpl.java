package com.xxf.view.loading;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;

import com.xxf.arch.widget.progresshud.ProgressHUD;

import java.util.concurrent.TimeUnit;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @version 2.0.1
 * @Description 加载对话框
 * @date createTime：2019/4/11
 */
public class DefaultProgressHUDImpl implements ProgressHUD {

    private AlphaLoading progressHUD;
    public Context actionContext;

    @Nullable
    protected Context getActionContext() {
        return actionContext;
    }

    public DefaultProgressHUDImpl() {
    }

    public DefaultProgressHUDImpl(@NonNull Context context) {
        attachContext(context);
    }

    @UiThread
    public void attachContext(@NonNull Context context) {
        this.actionContext = context;
    }

    @UiThread
    public void detachedContext() {
        if (progressHUD != null) {
            progressHUD.dismissImmediatelyLossState();
        }
        this.actionContext = null;
    }

    /**
     * 获取 菊花加载对话框
     *
     * @return
     */
    @NonNull
    protected final AlphaLoading getICourtProgressHUD() {
        if (progressHUD == null) {
            progressHUD = createProgressHUD(actionContext);
        }
        return progressHUD;
    }

    public static AlphaLoading createProgressHUD(Context actionContext) {
        return new AlphaLoading.Builder(actionContext)
                .cancelable(false)                           // 是否可以手动取消(点击空白区域或返回键)
                .resultDuration(TimeUnit.SECONDS.toMillis(1))  // ok/fail持续时间(milliseconds)
                .create();
    }

    /***
     *  展示加载对话框
     * @param notice
     */
    @UiThread
    @Override
    public void showLoadingDialog(@Nullable String notice) {
        AlphaLoading currSVProgressHUD = getICourtProgressHUD();
        currSVProgressHUD.setMessage(notice);
        if (!currSVProgressHUD.isShowing()) {
            currSVProgressHUD.show();
        }
    }

    /**
     * 取消加载对话框
     */
    @UiThread
    @Override
    public void dismissLoadingDialog() {
        if (isShowLoading()) {
            progressHUD.dismissImmediately();
        }
    }

    @Override
    public void dismissLoadingDialogWithSuccess(String notice) {
        dismissICourtLoadingDialogWithSuccess(notice, null);
    }

    public void dismissICourtLoadingDialogWithSuccess(String notice, Runnable endAction) {
        if (isShowLoading()) {
            progressHUD.dismissOk(notice, endAction);
        }
    }

    @Override
    public void dismissLoadingDialogWithFail(String notice) {
        dismissLoadingDialogWithFail(notice, null);
    }

    public void dismissLoadingDialogWithFail(String notice, Runnable endAction) {
        if (isShowLoading()) {
            progressHUD.dismissFail(notice, endAction);
        }
    }

    /**
     * 加载对话框是否展示中
     *
     * @return
     */
    @Override
    public boolean isShowLoading() {
        return progressHUD != null && progressHUD.isShowing();
    }

}
