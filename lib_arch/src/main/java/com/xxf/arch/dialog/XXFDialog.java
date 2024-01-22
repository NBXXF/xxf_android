package com.xxf.arch.dialog;


import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Pair;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialog;

import com.xxf.arch.component.ObservableComponent;
import com.xxf.arch.component.WindowComponent;
import com.xxf.utils.RAUtils;
import com.xxf.view.round.CornerUtil;

import org.jetbrains.annotations.NotNull;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;


/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description 推荐使用setResult
 * 建议是dialogFragment
 */
public class XXFDialog<R>
        extends AppCompatDialog
        implements ObservableComponent<AppCompatDialog, R>, WindowComponent {
    private final String TAG_PREFIX = "show_rau_";
    private final Subject<Object> componentSubject = PublishSubject.create().toSerialized();

    @Override
    public Observable<Pair<AppCompatDialog, R>> getComponentObservable() {
        return componentSubject.ofType(Object.class)
                .map(new Function<Object, Pair<AppCompatDialog, R>>() {
                    @Override
                    public Pair<AppCompatDialog, R> apply(Object o) throws Throwable {
                        return (Pair<AppCompatDialog, R>) o;
                    }
                });
    }

    @Override
    public void setComponentResult(R result) {
        componentSubject.onNext(Pair.create(this, result));
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
    public void show() {
        // 不能拦截  多个dialog 嵌套在dialogFragmennt中这样有问题 (RAUtils.INSTANCE.isLegal(TAG_PREFIX + this.getClass().getName(), RAUtils.DURATION_DEFAULT))
//        if (RAUtils.INSTANCE.isLegal(TAG_PREFIX + this.getClass().getName(), RAUtils.DURATION_DEFAULT)) {
//            super.show();
//        }
        super.show();
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
            WindowManager.LayoutParams attributes =window.getAttributes();
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

    @Override
    public void setWindowDimAmount(float amount) {
        Window window = getWindow();
        if (window != null) {
            window.setDimAmount(amount);
        }
    }

    @Override
    public void setWindowGravity(int gravity) {
        Window window = getWindow();
        if (window != null) {
            window.setGravity(gravity);
        }
    }

    @Override
    public void setWindowBackground(@NotNull Drawable drawable) {
        Window window = getWindow();
        if (window != null) {
            window.setBackgroundDrawable(drawable);
        }
    }

    @Override
    public void setWindowBackground(int color) {
        Window window = getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(color));
        }
    }

    @Override
    public void setWindowBackgroundDimEnabled(boolean enabled) {
        Window window = getWindow();
        if (window != null) {
            if (enabled) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            } else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            }
        }
    }

    @Override
    public void setWindowRadius(float radius) {
        FrameLayout decorView = getDecorView();
        if (decorView != null) {
            CornerUtil.INSTANCE.clipViewRadius(decorView,radius);
        }
    }
}
