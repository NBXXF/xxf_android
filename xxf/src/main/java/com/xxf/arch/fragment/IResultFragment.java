package com.xxf.arch.fragment;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import io.reactivex.rxjava3.functions.BiConsumer;


public interface IResultFragment<V extends Fragment, R> {

    /**
     * 设置回调监听
     *
     * @param consumer
     */
    void setFragmentConsumer(@Nullable BiConsumer<V, R> consumer);

    /**
     * 确定 完成
     *
     * @param r 成功参数
     */
    void setResult(R r);

}
