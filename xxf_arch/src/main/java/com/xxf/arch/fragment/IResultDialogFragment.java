package com.xxf.arch.fragment;

public interface IResultDialogFragment<R> {
    /**
     * 确定 完成
     *
     * @param r 成功参数
     */
    void setResult(R r);

}
