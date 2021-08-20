package com.xxf.arch.rxjava.transformer;

import com.xxf.arch.rxjava.transformer.internal.UILifeTransformerImpl;

import io.reactivex.rxjava3.functions.BiConsumer;
import io.reactivex.rxjava3.functions.Consumer;


/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description
 */
public class UIErrorTransformer<T> extends UILifeTransformerImpl<T> {
    public static int FLAG=Integer.MIN_VALUE;

    BiConsumer<Integer, ? super Throwable> consumer;
    public UIErrorTransformer( BiConsumer<Integer, ? super Throwable> consumer) {
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
                consumer.accept(FLAG,throwable);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public final void onCancel() {

    }
}
