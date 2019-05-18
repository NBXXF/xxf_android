package com.xxf.arch.rxjava.transformer.internal;

import android.support.annotation.UiThread;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description Rx与UI相关转换器
 */
public interface UILifeTransformer {
    /**
     * 开始执行
     */
    @UiThread
    void onSubscribe();

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
