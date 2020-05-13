package com.xxf.arch.widget.progresshud;

import androidx.annotation.Nullable;

/**
 * Description  加载对话框定义
 * author  youxuan  E-mail:xuanyouwu@163.com
 * date createTime：17/2/4
 * version
 */

public interface ProgressHUD {

    /**
     * 展示加载对话框
     *
     * @param notice
     */
    void showLoadingDialog(@Nullable String notice);

    /**
     * 结束展示对话框
     */
    void dismissLoadingDialog();

    /**
     * 加载成功的提示
     *
     * @param notice
     */
    void dismissLoadingDialogWithSuccess(String notice);

    /**
     * 加载失败的提示
     *
     * @param notice
     */
    void dismissLoadingDialogWithFail(String notice);

    /**
     * 是否正在展示加载对话框
     *
     * @return
     */
    boolean isShowLoading();
}
