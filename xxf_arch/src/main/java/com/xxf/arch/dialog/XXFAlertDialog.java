package com.xxf.arch.dialog;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.LifecycleOwner;

import com.xxf.arch.widget.progresshud.ProgressHUD;
import com.xxf.arch.widget.progresshud.ProgressHUDFactory;
import com.xxf.arch.widget.progresshud.ProgressHUDProvider;

import io.reactivex.functions.BiConsumer;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 推荐使用setResult
 */
public  class XXFAlertDialog<R>
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


    @Override
    public ProgressHUD progressHUD() {
        Context realContext = null;
        if (this.getOwnerActivity() != null) {
            realContext = this.getOwnerActivity();
        } else if (this.getContext() instanceof ContextWrapper) {
            realContext = ((ContextWrapper) this.getContext()).getBaseContext();
        } else {
            realContext = this.getContext();
        }
        if (realContext instanceof LifecycleOwner) {
            return ProgressHUDFactory.getInstance().getProgressHUD((LifecycleOwner) realContext);
        }
        return null;
    }
}
