package com.xxf.arch.dialog;

import android.content.Context;
import android.content.ContextWrapper;
import android.util.Pair;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.xxf.arch.component.ObservableComponent;
import com.xxf.arch.component.WindowComponent;
import com.xxf.utils.RAUtils;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;


/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description 推荐使用setResult
 */
public  class XXFAlertDialog<R>
        extends AlertDialog
        implements ObservableComponent<AlertDialog,R>, WindowComponent {
    private final String TAG_PREFIX = "show_rau_";
    private final Subject<Object> componentSubject = PublishSubject.create().toSerialized();

    @Override
    public Observable<Pair<AlertDialog, R>> getComponentObservable() {
        return componentSubject.ofType(Object.class)
                .map(new Function<Object, Pair<AlertDialog,  R>>() {
                    @Override
                    public Pair<AlertDialog,  R> apply(Object o) throws Throwable {
                        return Pair.create(XXFAlertDialog.this, (R) o);
                    }
                });
    }

    @Override
    public void setComponentResult(R result) {
        if (result != null) {
            componentSubject.onNext(result);
        }
    }

    protected XXFAlertDialog(@NonNull Context context) {
        super(context);
    }

    protected XXFAlertDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected XXFAlertDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public void show() {
        if (RAUtils.INSTANCE.isLegal(TAG_PREFIX + this.getClass().getName(), RAUtils.DURATION_DEFAULT)) {
            super.show();
        }
    }

    @Override
    public void setWindowSize(int width, int height) {
        Window window = getWindow();
        if(window!=null) {
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.width = width;
            attributes.height = height;
            window.setAttributes(attributes);
        }
    }

    @Override
    public void setWindowWidth(int width) {
        Window window = getWindow();
        if(window!=null) {
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.width = width;
            window.setAttributes(attributes);
        }
    }

    @Override
    public void setWindowHeight(int height) {
        Window window = getWindow();
        if(window!=null) {
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.height = height;
            window.setAttributes(attributes);
        }
    }


    @Nullable
    @Override
    public FrameLayout getDecorView() {
        Window window = getWindow();
        if(window!=null){
            return (FrameLayout) window.getDecorView();
        }
        return null;
    }

    @Nullable
    @Override
    public FrameLayout getContentParent() {
        Window window = getWindow();
        if(window!=null){
            return (FrameLayout) window.findViewById(android.R.id.content);
        }
        return null;
    }
}
