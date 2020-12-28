package com.xxf.arch.fragment;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import io.reactivex.rxjava3.functions.BiConsumer;


/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.1
 * @Description 支持返回值的AlertDialogFragment
 * @date createTime：2018/9/7
 */

public class XXFResultAlertDialogFragment<R> extends XXFAlertDialogFragment implements IResultDialogFragment<R> {

    private BiConsumer<DialogFragment, R> dialogFragmentConsumer;

    public XXFResultAlertDialogFragment(@Nullable BiConsumer<DialogFragment, R> dialogFragmentConsumer) {
        this.dialogFragmentConsumer = dialogFragmentConsumer;
    }

    /**
     * t提供默认的构造方法
     */
    public XXFResultAlertDialogFragment() {
    }

    @Override
    public  void setFragmentConsumer(@Nullable BiConsumer<DialogFragment, R> consumer) {
        this.dialogFragmentConsumer = consumer;
    }

    @Override
    public final void setResult(R r) {
        if (dialogFragmentConsumer != null) {
            try {
                dialogFragmentConsumer.accept(this, r);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        } else {
            dismiss();
        }
    }
}
