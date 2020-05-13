package com.xxf.arch.rxjava.transformer.internal;

import androidx.annotation.UiThread;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description Rx与UI相关转换器
 */
public interface UILifeTransformer<T> {
    /**
     * 开始执行
     */
    @UiThread
    void onSubscribe();

    /**
     * 接收到数据(可能多次)执行
     *
     * @param t
     */
    @UiThread
    void onNext(T t);

    /**
     * 执行结束
     */
    @UiThread
    void onComplete();

    /**
     * 执行失败
     *
     * @param throwable
     */
    @UiThread
    void onError(Throwable throwable);

    /**
     * 执行取消
     */
    @UiThread
    void onCancel();
}
