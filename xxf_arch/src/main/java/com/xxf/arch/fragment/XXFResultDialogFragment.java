package com.xxf.arch.fragment;

import androidx.fragment.app.DialogFragment;

import io.reactivex.functions.BiConsumer;

/**
 * @author xuanyouwu@163.com
 * @version 2.3.1
 * @Description 支持返回值的DialogFragment
 * @date createTime：2018/9/7
 */

public class XXFResultDialogFragment<R> extends XXFDialogFragment implements IResultDialogFragment<R> {

    private BiConsumer<DialogFragment, R> dialogConsumer;

    public XXFResultDialogFragment(BiConsumer<DialogFragment, R> dialogConsumer) {
        this.dialogConsumer = dialogConsumer;
    }

    /**
     * t提供默认的构造方法
     */
    public XXFResultDialogFragment() {
    }

    @Override
    public final void setResult(R r) {
        if (dialogConsumer != null) {
            try {
                dialogConsumer.accept(this, r);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            dismiss();
        }
    }
}
