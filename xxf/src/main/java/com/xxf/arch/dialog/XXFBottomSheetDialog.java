package com.xxf.arch.dialog;

import android.content.Context;
import android.content.ContextWrapper;
import android.util.Pair;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.xxf.arch.component.ObservableComponent;
import com.xxf.arch.widget.progresshud.ProgressHUD;
import com.xxf.arch.widget.progresshud.ProgressHUDFactory;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;

/**
 * Author: 炫神
 * Date: 1/6/21 7:22 PM
 * Description: 带滑动手势的SheetDialog BottomSheetDialog
 */
public class XXFBottomSheetDialog<R> extends BottomSheetDialog
        implements ObservableComponent<BottomSheetDialog, R> {
    private final Subject<Object> componentSubject = PublishSubject.create().toSerialized();

    @Override
    public Observable<Pair<BottomSheetDialog, R>> getComponentObservable() {
        return componentSubject.ofType(Object.class)
                .map(new Function<Object, Pair<BottomSheetDialog, R>>() {
                    @Override
                    public Pair<BottomSheetDialog, R> apply(Object o) throws Throwable {
                        return Pair.create(XXFBottomSheetDialog.this, (R) o);
                    }
                });
    }

    @Override
    public void setComponentResult(R result) {
        if (result != null) {
            componentSubject.onNext(result);
        }
    }

    protected XXFBottomSheetDialog(@NonNull Context context) {
        super(context);
    }

    protected XXFBottomSheetDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected XXFBottomSheetDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @NonNull
    @Override
    public BottomSheetBehavior<FrameLayout> getBehavior() {
        return super.getBehavior();
    }
}
