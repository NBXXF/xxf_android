package com.xxf.arch.rxjava.transformer;

import com.xxf.arch.rxjava.transformer.internal.UILifeTransformerImpl;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 空实现
 */
public class EmptyUILifeTransformer<T> extends UILifeTransformerImpl<T> {
    @Override
    public void onSubscribe() {

    }

    @Override
    public void onNext(T t) {

    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onCancel() {

    }
}
