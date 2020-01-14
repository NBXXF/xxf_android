package com.xxf.arch.rxjava.transformer;

import com.xxf.arch.rxjava.transformer.internal.UILifeTransformerImpl;

import org.reactivestreams.Publisher;

import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.CompletableTransformer;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.MaybeTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class UIErrorTransformer<T> extends UILifeTransformerImpl<T> {

    Consumer<? super Throwable> consumer;

    public UIErrorTransformer(Consumer<? super Throwable> consumer) {
        this.consumer = consumer;
    }


    @Override
    public final void onSubscribe() {

    }

    @Override
    public final void onNext(T t) {

    }

    @Override
    public final void onComplete() {

    }

    @Override
    public final void onError(Throwable throwable) {
        if (consumer != null) {
            try {
                consumer.accept(throwable);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public final void onCancel() {

    }
}
