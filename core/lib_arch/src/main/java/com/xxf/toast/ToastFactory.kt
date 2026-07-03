package com.xxf.toast

import android.content.Context
import android.view.View
import com.xxf.snackbar.Snackbar

interface ToastFactory {
    /**
     * @param msg
     * @param type
     * @param context
     * @param flag               任意类型的整形 可以标记toast业务类型 比如位置,也可以根据后端返回的状态码进行颜色 着重文字提示
     * 默认Gravity.CENTER
     * XXF.bindToErrorNotice 是Gravity.CENTER
     * flag的定值 可以参考【Gravity】类的枚举值
     *
     *
     * flag 等价于ErrorHandler 返回的flag  XXF.init().setErrorHandler(new BiConsumer<Integer></Integer>, Throwable>() {
     * @return
     * @Override public void accept(Integer flag, Throwable throwable) throws Throwable {
     * ToastUtils.showToast("error:" + throwable, ToastUtils.ToastType.ERROR,flag);
     * }
     * }));
     */
    fun createToast(
        msg: CharSequence,
        type: ToastType,
        context: Context,
        flag: Int
    ): LimitToast

    /**
     * 当 系统通知关闭后 会主动调snackBar
     */
    fun createSnackbar(
        rootView: View,
        msg: CharSequence,
        type: ToastType,
        context: Context,
        flag: Int
    ): Snackbar;
}
