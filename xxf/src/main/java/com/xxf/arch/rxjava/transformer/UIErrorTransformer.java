package com.xxf.arch.rxjava.transformer;

import com.xxf.arch.rxjava.transformer.internal.UILifeTransformerImpl;

import io.reactivex.rxjava3.functions.BiConsumer;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description
 */
public class UIErrorTransformer<T> extends UILifeTransformerImpl<T> {
    /**
     * 忽略网络错误的标记位,默认值
     */
    public static final int FLAG_IGNORE_NET_ERROR = Integer.MIN_VALUE;

    BiConsumer<Integer, ? super Throwable> consumer;
    int flag = FLAG_IGNORE_NET_ERROR;

    public UIErrorTransformer(BiConsumer<Integer, ? super Throwable> consumer) {
        this.consumer = consumer;
    }

    public UIErrorTransformer(BiConsumer<Integer, ? super Throwable> consumer, int flag) {
        this.consumer = consumer;
        this.flag = flag;
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
                consumer.accept(flag, throwable);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public final void onCancel() {

    }
}
