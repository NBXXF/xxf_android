package com.xxf.arch.rxjava.transformer;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.MaybeTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
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
public abstract class SubscribeLifeTransformer<T>
        implements
        ObservableTransformer<T, T>,
        FlowableTransformer<T, T>,
        MaybeTransformer<T, T> {

    /**
     * 开始执行
     */
    protected abstract void onSubscribe();

    /**
     * 执行结束
     */
    protected abstract void onComplete();

    /**
     * 执行失败
     *
     * @param throwable
     */
    protected abstract void onError(Throwable throwable);

    /**
     * 执行取消
     */
    protected void onCancel() {

    }

    @Override
    public Publisher<T> apply(Flowable<T> upstream) {
        return upstream
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
                        onSubscribe();
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
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        onSubscribe();
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
        return upstream
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        onSubscribe();
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

}
