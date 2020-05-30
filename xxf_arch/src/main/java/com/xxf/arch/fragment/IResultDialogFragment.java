package com.xxf.arch.fragment;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import io.reactivex.functions.BiConsumer;

public interface IResultDialogFragment<R> {

    /**
     * 设置回调监听
     * @param consumer
     */
    void setDialogFragmentConsumer(@Nullable BiConsumer<DialogFragment, R> consumer);

    /**
     * 确定 完成
     *
     * @param r 成功参数
     */
    void setResult(R r);

}
