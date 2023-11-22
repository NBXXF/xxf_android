package com.xxf.arch.rxjava.transformer;

import android.view.Gravity;

import com.xxf.arch.rxjava.transformer.filter.ErrorNoFilter;
import com.xxf.arch.rxjava.transformer.internal.UILifeTransformerImpl;

import io.reactivex.rxjava3.functions.BiConsumer;
import io.reactivex.rxjava3.functions.Predicate;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description
 */
public class UIErrorTransformer<T> extends UILifeTransformerImpl<T> {
    BiConsumer<Integer, ? super Throwable> consumer;
    int toastFlag = Gravity.CENTER;
    Predicate<Throwable> filter = ErrorNoFilter.INSTANCE;

    public UIErrorTransformer(BiConsumer<Integer, ? super Throwable> consumer) {
        this.consumer = consumer;
    }

    public UIErrorTransformer(BiConsumer<Integer, ? super Throwable> consumer, int toastFlag) {
        this.consumer = consumer;
        this.toastFlag = toastFlag;
    }

    public UIErrorTransformer(BiConsumer<Integer, ? super Throwable> consumer, int toastFlag, Predicate<Throwable> filter) {
        this.consumer = consumer;
        this.toastFlag = toastFlag;
        this.filter = filter;
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
        try {
            /**
             * 过滤一下
             */
            if (this.filter.test(throwable)) {
                if (consumer != null) {
                    consumer.accept(toastFlag, throwable);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public final void onCancel() {

    }
}
