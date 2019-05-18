package com.xxf.arch.rxjava.transformer;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.xxf.arch.rxjava.transformer.internal.UILifeTransformerImpl;
import com.xxf.arch.widget.progresshud.ProgressHUD;
import com.xxf.arch.widget.progresshud.ProgressHUDProvider;

import org.reactivestreams.Publisher;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @version 2.3.0
 * @Description 加载与loading提示结合
 * @date createTime：2018/1/4
 */
public class ProgressHUDTransformerImpl<T> extends UILifeTransformerImpl<T> {

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

    @Override
    public void onSubscribe() {
        if (progressHUD != null) {
            progressHUD.showLoadingDialog(loadingNotice);
        }
    }

    @Override
    public void onComplete() {
        if (progressHUD != null) {
            progressHUD.dismissLoadingDialogWithSuccess(successNotice);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        if (progressHUD != null) {
            if (TextUtils.isEmpty(errorNotice)) {
                progressHUD.dismissLoadingDialogWithFail(throwable.getMessage());
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

    @Override
    public MaybeSource<T> apply(Maybe<T> upstream) {
        return super.apply(upstream.delay(1, TimeUnit.SECONDS));
    }

    @Override
    public ObservableSource<T> apply(Observable<T> upstream) {
        return super.apply(upstream.delay(1, TimeUnit.SECONDS));
    }

    @Override
    public Publisher<T> apply(Flowable<T> upstream) {
        return super.apply(upstream.delay(1, TimeUnit.SECONDS));
    }

}
