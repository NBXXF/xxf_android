package com.xxf.arch.rxjava.transformer;

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
public class UIErrorTransformer<T> implements
        ObservableTransformer<T, T>,
        FlowableTransformer<T, T>,
        MaybeTransformer<T, T>,
        CompletableTransformer {

    Consumer<? super Throwable> consumer;

    public UIErrorTransformer(Consumer<? super Throwable> consumer) {
        this.consumer = consumer;
    }

    @Override
    public CompletableSource apply(Completable upstream) {
        return upstream.observeOn(AndroidSchedulers.mainThread())
                .doOnError(this.consumer);
    }

    @Override
    public Publisher<T> apply(Flowable<T> upstream) {
        return upstream.observeOn(AndroidSchedulers.mainThread())
                .doOnError(this.consumer);
    }

    @Override
    public MaybeSource<T> apply(Maybe<T> upstream) {
        return upstream.observeOn(AndroidSchedulers.mainThread())
                .doOnError(this.consumer);
    }

    @Override
    public ObservableSource<T> apply(Observable<T> upstream) {
        return upstream.observeOn(AndroidSchedulers.mainThread())
                .doOnError(this.consumer);
    }
}
