package com.xxf.arch.dialog;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 带处理结果的dialog
 */
public interface IResultDialog<R extends Serializable> extends DialogInterface {

    /**
     * 取消
     *
     * @param cancelResult 取消参数
     */
    void cancel(R cancelResult);

    /**
     * 确定 完成
     *
     * @param confirmResult 成功参数
     */
    void confirm(R confirmResult);

    /**
     * 比{@link DialogInterface.OnClickListener }多一个结果处理 和拦截
     *
     * @param <R>
     */
    interface OnDialogClickListener<R extends Serializable> {

        /**
         * 取消
         *
         * @param dialog
         * @param cancelResult 取消参数
         * @return true 代表自己处理关闭逻辑,false:自动关闭
         */
        boolean onCancel(@NonNull DialogInterface dialog, @Nullable R cancelResult);

        /**
         * 确定 完成
         *
         * @param dialog
         * @param confirmResult 成功参数
         * @return true 代表自己处理关闭逻辑,false:自动关闭
         */
        boolean onConfirm(@NonNull DialogInterface dialog, @Nullable R confirmResult);
    }
}
