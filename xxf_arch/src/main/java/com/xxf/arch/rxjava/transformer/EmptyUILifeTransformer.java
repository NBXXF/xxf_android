package com.xxf.arch.rxjava.transformer;

import com.xxf.arch.rxjava.transformer.internal.UILifeTransformerImpl;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
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
