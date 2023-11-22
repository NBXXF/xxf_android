package com.xxf.arch.widget.progresshud;

import androidx.annotation.Nullable;

/**
 * Description  加载对话框定义
 *
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：17/2/4
 * version
 */

public interface ProgressHUD {
    ProgressHUD EMPTY = new ProgressHUD() {
        @Override
        public void showLoadingDialog(@Nullable String notice) {

        }

        @Override
        public void dismissLoadingDialog() {

        }

        @Override
        public void dismissLoadingDialogWithSuccess(String notice, long delayTime) {

        }

        @Override
        public void dismissLoadingDialogWithFail(String notice, long delayTime) {

        }

        @Override
        public boolean isShowLoading() {
            return false;
        }

        @Override
        public void updateStateText(String notice) {

        }
    };

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
     * @param delayTime 延迟时间 毫秒
     */
    void dismissLoadingDialogWithSuccess(String notice, long delayTime);

    /**
     * 加载失败的提示
     *
     * @param notice
     * @param delayTime 延迟时间 毫秒
     */
    void dismissLoadingDialogWithFail(String notice, long delayTime);

    /**
     * 是否正在展示加载对话框
     *
     * @return
     */
    boolean isShowLoading();


    /**
     * 更新提示文案
     *
     * @param notice
     */
    void updateStateText(String notice);
}
