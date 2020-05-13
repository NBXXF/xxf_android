package com.xxf.arch.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.xxf.arch.widget.progresshud.ProgressHUDProvider;

import io.reactivex.functions.BiConsumer;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 推荐使用setResult
 */
public abstract class XXFDialog<R>
        extends Dialog
        implements ProgressHUDProvider, IResultDialog<R> {

    private BiConsumer<DialogInterface, R> dialogConsumer;

    protected XXFDialog(@NonNull Context context, @Nullable BiConsumer<DialogInterface, R> dialogConsumer) {
        super(context);
        this.dialogConsumer = dialogConsumer;
    }

    protected XXFDialog(@NonNull Context context, int themeResId, @Nullable BiConsumer<DialogInterface, R> dialogConsumer) {
        super(context, themeResId);
        this.dialogConsumer = dialogConsumer;
    }

    protected XXFDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener, @Nullable BiConsumer<DialogInterface, R> onDialogClickListener) {
        super(context, cancelable, cancelListener);
        this.dialogConsumer = onDialogClickListener;
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
