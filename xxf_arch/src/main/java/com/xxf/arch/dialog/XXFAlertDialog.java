package com.xxf.arch.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

import com.xxf.arch.widget.progresshud.ProgressHUDProvider;


import io.reactivex.functions.BiConsumer;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 推荐使用setResult
 */
public abstract class XXFAlertDialog<R>
        extends AlertDialog
        implements ProgressHUDProvider, IResultDialog<R> {

    private BiConsumer<DialogInterface, R> dialogConsumer;

    protected XXFAlertDialog(@NonNull Context context, @Nullable BiConsumer<DialogInterface, R> dialogConsumer) {
        super(context);
        this.dialogConsumer = dialogConsumer;
    }

    protected XXFAlertDialog(@NonNull Context context, int themeResId, @Nullable BiConsumer<DialogInterface, R> dialogConsumer) {
        super(context, themeResId);
        this.dialogConsumer = dialogConsumer;
    }

    protected XXFAlertDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener, @Nullable BiConsumer<DialogInterface, R> dialogConsumer) {
        super(context, cancelable, cancelListener);
        this.dialogConsumer = dialogConsumer;
    }

    /**
     * 分发确认结果
     *
     * @param confirmResult
     * @return
     */
    @Override
    public final void setResult(R confirmResult) {
        if (dialogConsumer != null) {
            try {
                dialogConsumer.accept(this, confirmResult);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            dismiss();
        }
    }
}
