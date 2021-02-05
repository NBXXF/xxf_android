package com.xxf.arch.rxjava.transformer;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;

import com.xxf.arch.XXF;
import com.xxf.arch.rxjava.transformer.internal.UILifeTransformerImpl;
import com.xxf.arch.utils.HandlerUtils;
import com.xxf.arch.widget.progresshud.ProgressHUD;
import com.xxf.arch.widget.progresshud.ProgressHUDFactory;
import com.xxf.arch.widget.progresshud.ProgressHUDProvider;

import org.reactivestreams.Publisher;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.MaybeSource;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.functions.Function;

/**
 * @version 2.3.0
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description 加载与loading提示结合
 * @date createTime：2018/1/4
 */
public class ProgressHUDTransformerImpl<T> extends UILifeTransformerImpl<T> {
    public static volatile ProgressHUDTransformerImpl EMPTY = new ProgressHUDTransformerImpl((ProgressHUD) null);

    ProgressHUD progressHUD;
    String loadingNotice = "loading...";
    /**
     * 为null将会用真正的错误信息
     * 既不想用真正的错误信息 也不展示传递错误信息 可以传递 双引号""
     */
    String errorNotice = null;
    String successNotice = "success";
    long noticeDuration = 1000;
    LifecycleOwner lifecycleOwner;
    boolean dismissOnNext = true;

    public ProgressHUDTransformerImpl(ProgressHUD progressHUD) {
        this.progressHUD = progressHUD;
    }

    public ProgressHUDTransformerImpl(@NonNull LifecycleOwner lifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner;
    }

    public ProgressHUDTransformerImpl(@NonNull ProgressHUDProvider progressHUDProvider) {
        this.progressHUD = progressHUDProvider.progressHUD();
    }

    public ProgressHUDTransformerImpl<T> setLoadingNotice(@Nullable String loadingNotice) {
        this.loadingNotice = loadingNotice;
        return this;
    }

    /**
     * 为null将会用真正的错误信息
     * 既不想用真正的错误信息 也不展示传递的错误信息 可以传递 双引号""
     */
    public ProgressHUDTransformerImpl<T> setErrorNotice(@Nullable String errorNotice) {
        this.errorNotice = errorNotice;
        return this;
    }

    public ProgressHUDTransformerImpl<T> setSuccessNotice(@Nullable String successNotice) {
        this.successNotice = successNotice;
        return this;
    }

    public ProgressHUDTransformerImpl<T> setNoticeDuration(long noticeDuration) {
        this.noticeDuration = noticeDuration < 0 ? 0 : noticeDuration;
        return this;
    }

    public ProgressHUDTransformerImpl<T> setDismissOnNext(boolean dismissOnNext) {
        this.dismissOnNext = dismissOnNext;
        return this;
    }

    @Nullable
    @CheckResult
    protected ProgressHUD getSafeProgressHUD() {
        if (!HandlerUtils.isMainThread()) {
            return null;
        }
        if (progressHUD != null) {
            return progressHUD;
        }
        if (lifecycleOwner != null) {
            progressHUD = ProgressHUDFactory.getInstance().getProgressHUD(lifecycleOwner);
            return progressHUD;
        }
        return null;
    }

    @Override
    public void onSubscribe() {
        if (HandlerUtils.isMainThread()) {
            ProgressHUD safeProgressHUD = getSafeProgressHUD();
            if (safeProgressHUD != null) {
                safeProgressHUD.showLoadingDialog(loadingNotice);
            }
        } else {
            HandlerUtils.getMainHandler().post(new Runnable() {
                @Override
                public void run() {
                    ProgressHUD safeProgressHUD = getSafeProgressHUD();
                    if (safeProgressHUD != null) {
                        safeProgressHUD.showLoadingDialog(loadingNotice);
                    }
                }
            });
        }
    }


    @Override
    public void onNext(T t) {
        if (dismissOnNext) {
            if (HandlerUtils.isMainThread()) {
                ProgressHUD safeProgressHUD = getSafeProgressHUD();
                if (safeProgressHUD != null) {
                    safeProgressHUD.dismissLoadingDialogWithSuccess(successNotice, noticeDuration);
                }
            } else {
                HandlerUtils.getMainHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        ProgressHUD safeProgressHUD = getSafeProgressHUD();
                        if (safeProgressHUD != null) {
                            safeProgressHUD.dismissLoadingDialogWithSuccess(successNotice, noticeDuration);
                        }
                    }
                });
            }
        }
    }

    @Override
    public void onComplete() {
        if (HandlerUtils.isMainThread()) {
            ProgressHUD safeProgressHUD = getSafeProgressHUD();
            if (safeProgressHUD != null) {
                safeProgressHUD.dismissLoadingDialogWithSuccess(successNotice, noticeDuration);
            }
        } else {
            HandlerUtils.getMainHandler().post(new Runnable() {
                @Override
                public void run() {
                    ProgressHUD safeProgressHUD = getSafeProgressHUD();
                    if (safeProgressHUD != null) {
                        safeProgressHUD.dismissLoadingDialogWithSuccess(successNotice, noticeDuration);
                    }
                }
            });
        }
    }

    @Override
    public void onError(Throwable throwable) {
        if (errorNotice == null) {
            try {
                errorNotice = XXF.getErrorConvertFunction().apply(throwable);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        if (HandlerUtils.isMainThread()) {
            ProgressHUD safeProgressHUD = getSafeProgressHUD();
            if (safeProgressHUD != null) {
                safeProgressHUD.dismissLoadingDialogWithFail(errorNotice, noticeDuration);
            }
        } else {
            HandlerUtils.getMainHandler().post(new Runnable() {
                @Override
                public void run() {
                    ProgressHUD safeProgressHUD = getSafeProgressHUD();
                    if (safeProgressHUD != null) {
                        safeProgressHUD.dismissLoadingDialogWithFail(errorNotice, noticeDuration);
                    }
                }
            });
        }
    }

    @Override
    public void onCancel() {
        if (HandlerUtils.isMainThread()) {
            ProgressHUD safeProgressHUD = getSafeProgressHUD();
            if (safeProgressHUD != null) {
                safeProgressHUD.dismissLoadingDialog();
            }
        } else {
            HandlerUtils.getMainHandler().post(new Runnable() {
                @Override
                public void run() {
                    ProgressHUD safeProgressHUD = getSafeProgressHUD();
                    if (safeProgressHUD != null) {
                        safeProgressHUD.dismissLoadingDialogWithFail(errorNotice, noticeDuration);
                    }
                }
            });
        }
        if (progressHUD != null) {
            progressHUD.dismissLoadingDialog();
        }
    }

    @Override
    public MaybeSource<T> apply(Maybe<T> upstream) {
        MaybeSource<T> apply = super.apply(upstream);
        if (apply instanceof Maybe) {
            return ((Maybe<T>) apply)
                    .onErrorResumeNext(new Function<Throwable, MaybeSource<? extends T>>() {
                        @Override
                        public MaybeSource<? extends T> apply(Throwable throwable) throws Throwable {
                            Maybe<T> error = Maybe.error(throwable);
                            error = error.delaySubscription(noticeDuration, TimeUnit.MILLISECONDS);
                            return error;
                        }
                    }).delay(noticeDuration, TimeUnit.MILLISECONDS);
        }
        return apply;
    }

    @Override
    public ObservableSource<T> apply(Observable<T> upstream) {
        ObservableSource<T> apply = super.apply(upstream);
        if (apply instanceof Observable) {
            return ((Observable<T>) apply)
                    .onErrorResumeNext(new Function<Throwable, ObservableSource<? extends T>>() {
                        @Override
                        public ObservableSource<? extends T> apply(Throwable throwable) throws Throwable {
                            Observable<T> observable = Observable.error(throwable);
                            observable = observable.delaySubscription(noticeDuration, TimeUnit.MILLISECONDS);
                            return observable;
                        }
                    }).delay(noticeDuration, TimeUnit.MILLISECONDS);
        }
        return apply;
    }

    @Override
    public Publisher<T> apply(Flowable<T> upstream) {
        Publisher<T> apply = super.apply(upstream);
        if (apply instanceof Flowable) {
            return ((Flowable<T>) apply)
                    .onErrorResumeNext(new Function<Throwable, Publisher<? extends T>>() {
                        @Override
                        public Publisher<? extends T> apply(Throwable throwable) throws Throwable {
                            Flowable<T> error = Flowable.error(throwable);
                            error = error.delaySubscription(noticeDuration, TimeUnit.MILLISECONDS);
                            return error;
                        }
                    }).delay(noticeDuration, TimeUnit.MILLISECONDS);
        }
        return apply;
    }

}
