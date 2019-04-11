package com.xxf.arch.rxjava.transformer;

import android.support.annotation.Nullable;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @version 2.3.0
 * @Description
 * @date createTime：2017/12/24
 */
public interface ProgressHUDTransformerProvider {
    /**
     * 默认
     *
     * @param <T>
     * @return
     */
    <T> SubscribeLifeTransformer<T> bindToProgressHUD();

    /**
     * @param loadingNotice 设置加载成功中的提示
     * @param <T>
     * @return
     */
    <T> SubscribeLifeTransformer<T> bindToProgressHUD(String loadingNotice);

    /**
     * @param loadingNotice
     * @param successNotice
     * @param <T>
     * @return
     */
    <T> SubscribeLifeTransformer<T> bindToProgressHUD(@Nullable String loadingNotice, @Nullable String successNotice);

    /**
     * @param loadingNotice
     * @param errorNotice
     * @param successNotice
     * @param <T>
     * @return
     */
    <T> SubscribeLifeTransformer<T> bindToProgressHUD(@Nullable String loadingNotice, @Nullable String errorNotice, @Nullable String successNotice);
}
