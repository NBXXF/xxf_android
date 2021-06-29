package com.xxf.arch.dialog;


import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialog;
import androidx.lifecycle.LifecycleOwner;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.xxf.arch.component.ObservableComponent;
import com.xxf.arch.widget.progresshud.ProgressHUD;
import com.xxf.arch.widget.progresshud.ProgressHUDFactory;
import com.xxf.arch.widget.progresshud.ProgressHUDProvider;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.BiConsumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;


/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description 推荐使用setResult
 */
public class XXFDialog<R>
        extends AppCompatDialog
        implements ObservableComponent<AppCompatDialog,R>, ProgressHUDProvider {

    private final Subject<Object> componentSubject = PublishSubject.create().toSerialized();
    @Override
    public Observable<Pair<AppCompatDialog, R>> getComponentObservable() {
        return componentSubject.ofType(Object.class)
                .map(new Function<Object, Pair<AppCompatDialog,  R>>() {
                    @Override
                    public Pair<AppCompatDialog,  R> apply(Object o) throws Throwable {
                        return Pair.create(XXFDialog.this, (R) o);
                    }
                });
    }

    @Override
    public void setComponentResult(R result) {
        if (result != null) {
            componentSubject.onNext(result);
        }
    }
    protected XXFDialog(@NonNull Context context) {
        super(context);
    }

    protected XXFDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected XXFDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
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
