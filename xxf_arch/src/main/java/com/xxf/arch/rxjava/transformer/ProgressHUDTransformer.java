package com.xxf.arch.rxjava.transformer;

import android.support.annotation.Nullable;
import android.text.TextUtils;

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
public class ProgressHUDTransformer<T> extends SubscribeLifeTransformer<T> {

    protected IProgressHUD iProgressHUD;
    protected String loadingNotice;
    protected String errorNotice;
    protected String successNotice;

    /**
     * @param iProgressHUD
     * @param loadingNotice 可空
     * @param errorNotice   如果为空,直接展示默认的error,否则展示errorNotice
     * @param successNotice 空 不展示正确对勾符号
     */
    public ProgressHUDTransformer(IProgressHUD iProgressHUD,
                                  @Nullable String loadingNotice,
                                  @Nullable String errorNotice,
                                  @Nullable String successNotice) {
        this.iProgressHUD = iProgressHUD;
        this.loadingNotice = loadingNotice;
        this.errorNotice = errorNotice;
        this.successNotice = successNotice;
    }

    @Override
    protected void onSubscribe() {
        if (iProgressHUD != null) {
            iProgressHUD.showLoadingDialog(loadingNotice);
        }
    }

    @Override
    protected void onComplete() {
        if (iProgressHUD != null) {
            iProgressHUD.dismissLoadingDialogWithSuccess(successNotice);
        }
    }

    @Override
    protected void onError(Throwable throwable) {
        if (iProgressHUD != null) {
            if (TextUtils.isEmpty(errorNotice)) {
                iProgressHUD.dismissLoadingDialogWithFail(throwable.getMessage());
            } else {
                iProgressHUD.dismissLoadingDialogWithFail(errorNotice);
            }
        }
    }

    @Override
    protected void onCancel() {
        if (iProgressHUD != null) {
            iProgressHUD.dismissLoadingDialog();
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
