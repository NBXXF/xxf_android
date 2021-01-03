package com.xxf.arch.rxjava.transformer;

import com.xxf.arch.rxjava.transformer.internal.UILifeTransformerImpl;

import io.reactivex.rxjava3.functions.Consumer;


/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
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
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public final void onCancel() {

    }
}
