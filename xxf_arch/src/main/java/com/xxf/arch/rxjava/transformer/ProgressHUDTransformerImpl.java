package com.xxf.arch.rxjava.transformer;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.xxf.arch.XXF;
import com.xxf.arch.rxjava.transformer.internal.UILifeTransformerImpl;
import com.xxf.arch.widget.progresshud.ProgressHUD;
import com.xxf.arch.widget.progresshud.ProgressHUDProvider;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.0
 * @Description 加载与loading提示结合
 * @date createTime：2018/1/4
 */
public class ProgressHUDTransformerImpl<T> extends UILifeTransformerImpl<T> {
    public static volatile ProgressHUDTransformerImpl EMPTY = new ProgressHUDTransformerImpl(null, null, null, null);

    /**
     * builder模式
     */
    public static class Builder {
        ProgressHUD progressHUD;
        String loadingNotice;
        String errorNotice;
        String successNotice;

        public Builder(@NonNull ProgressHUDProvider progressHUDProvider) {
            this.progressHUD = progressHUDProvider.progressHUD();
        }

        public Builder(@NonNull ProgressHUD progressHUD) {
            this.progressHUD = progressHUD;
        }

        public Builder setLoadingNotice(@Nullable String loadingNotice) {
            this.loadingNotice = loadingNotice;
            return this;
        }

        public Builder setErrorNotice(@Nullable String errorNotice) {
            this.errorNotice = errorNotice;
            return this;
        }

        public Builder setSuccessNotice(@Nullable String successNotice) {
            this.successNotice = successNotice;
            return this;
        }

        public <T> ProgressHUDTransformerImpl<T> build() {
            return new ProgressHUDTransformerImpl<T>(this.progressHUD, this.loadingNotice, this.errorNotice, this.successNotice);
        }
    }

    protected ProgressHUD progressHUD;
    protected String loadingNotice;
    protected String errorNotice;
    protected String successNotice;

    /**
     * @param progressHUD
     * @param loadingNotice 可空
     * @param errorNotice   如果为空,直接展示默认的error,否则展示errorNotice
     * @param successNotice 空 不展示正确对勾符号
     */
    public ProgressHUDTransformerImpl(ProgressHUD progressHUD,
                                      @Nullable String loadingNotice,
                                      @Nullable String errorNotice,
                                      @Nullable String successNotice) {
        this.progressHUD = progressHUD;
        this.loadingNotice = loadingNotice;
        this.errorNotice = errorNotice;
        this.successNotice = successNotice;
    }

    public void setLoadingNotice(String loadingNotice) {
        this.loadingNotice = loadingNotice;
    }

    public void setErrorNotice(String errorNotice) {
        this.errorNotice = errorNotice;
    }

    public void setSuccessNotice(String successNotice) {
        this.successNotice = successNotice;
    }

    @Override
    public void onSubscribe() {
        if (progressHUD != null) {
            progressHUD.showLoadingDialog(loadingNotice);
        }
    }

    @Override
    public void onNext(T t) {
        if (progressHUD != null) {
            progressHUD.dismissLoadingDialogWithSuccess(successNotice);
        }
    }

    @Override
    public void onComplete() {
        if (progressHUD != null && progressHUD.isShowLoading()) {
            progressHUD.dismissLoadingDialogWithSuccess(successNotice);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        if (progressHUD != null) {
            if (TextUtils.isEmpty(errorNotice)) {
                String parseErrorNotice = "";
                try {
                    parseErrorNotice = XXF.getErrorConvertFunction().apply(throwable);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                progressHUD.dismissLoadingDialogWithFail(parseErrorNotice);
            } else {
                progressHUD.dismissLoadingDialogWithFail(errorNotice);
            }
        }
    }

    @Override
    public void onCancel() {
        if (progressHUD != null) {
            progressHUD.dismissLoadingDialog();
        }
    }
}
