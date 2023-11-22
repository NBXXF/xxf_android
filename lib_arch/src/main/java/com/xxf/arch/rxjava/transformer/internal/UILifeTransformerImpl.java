package com.xxf.arch.rxjava.transformer.internal;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableSource;
import io.reactivex.rxjava3.core.CompletableTransformer;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.FlowableTransformer;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.MaybeSource;
import io.reactivex.rxjava3.core.MaybeTransformer;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.ObservableTransformer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;


/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.0
 * @Description <p>
 * <p>
 * <p>
 * 特性:
 * 1: 处理UI操作
 * 2：接收处理默认分发在主线程
 * @date createTime：2017/12/24
 * </p>
 */
public abstract class UILifeTransformerImpl<T>
        implements
        UILifeTransformer<T>,
        ObservableTransformer<T, T>,
        FlowableTransformer<T, T>,
        MaybeTransformer<T, T>,
        CompletableTransformer {


    @Override
    public Publisher<T> apply(Flowable<T> upstream) {
        /**
         * https://github.com/ReactiveX/RxJava/issues/3908
         * delayError=true
         */
        return upstream
                .observeOn(AndroidSchedulers.mainThread(),true)
                .unsubscribeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
                        onSubscribe();
                    }
                }).doOnNext(new Consumer<T>() {
                    @Override
                    public void accept(T t) throws Exception {
                        onNext(t);
                    }
                }).doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        onError(throwable);
                    }
                }).doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        onComplete();
                    }
                }).doOnCancel(new Action() {
                    @Override
                    public void run() throws Exception {
                        onCancel();
                    }
                });
    }

    @Override
    public MaybeSource<T> apply(Maybe<T> upstream) {
        return upstream
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        onSubscribe();
                    }
                }).doOnSuccess(new Consumer<T>() {
                    @Override
                    public void accept(T t) throws Exception {
                        onNext(t);
                    }
                }).doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        onError(throwable);
                    }
                }).doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        onComplete();
                    }
                }).doOnDispose(new Action() {
                    @Override
                    public void run() throws Exception {
                        onCancel();
                    }
                });
    }

    @Override
    public ObservableSource<T> apply(Observable<T> upstream) {
        /**
         * https://github.com/ReactiveX/RxJava/issues/3908
         * delayError=true
         */
        return upstream
                .observeOn(AndroidSchedulers.mainThread(),true)
                .unsubscribeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        onSubscribe();
                    }
                }).doOnNext(new Consumer<T>() {
                    @Override
                    public void accept(T t) throws Exception {
                        onNext(t);
                    }
                }).doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        onError(throwable);
                    }
                }).doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        onComplete();
                    }
                }).doOnDispose(new Action() {
                    @Override
                    public void run() throws Exception {
                        onCancel();
                    }
                });
    }

    @Override
    public CompletableSource apply(Completable upstream) {
        return upstream.observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        onSubscribe();
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        onError(throwable);
                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        onComplete();
                    }
                })
                .doOnDispose(new Action() {
                    @Override
                    public void run() throws Exception {
                        onCancel();
                    }
                });
    }

}
