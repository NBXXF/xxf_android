package com.xxf.arch.dialog;

import android.content.DialogInterface;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 带处理结果的dialog
 */
public interface IResultDialog<R> extends DialogInterface {


    /**
     * 确定 完成
     *
     * @param r 成功参数
     */
    void setResult(R r);

    /**
     * 不建议使用,建议使用 setResult
     */
    @Deprecated
    @Override
    void dismiss();
}
