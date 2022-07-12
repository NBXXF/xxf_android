package com.xxf.arch.dialog;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.xxf.arch.component.BottomSheetWindowComponent;
import com.xxf.arch.component.ObservableComponent;
import com.xxf.utils.RAUtils;

import org.jetbrains.annotations.NotNull;

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
        implements ObservableComponent<BottomSheetDialog, R>, BottomSheetWindowComponent {
    private final String TAG_PREFIX = "show_rau_";
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

    @Override
    public void show() {
        if (RAUtils.INSTANCE.isLegal(TAG_PREFIX + this.getClass().getName(), RAUtils.DURATION_DEFAULT)) {
            super.show();
        }
    }


    @Nullable
    @Override
    public FrameLayout getBottomSheetView() {
        View dec = getDecorView();
        if (dec != null) {
            return dec.findViewById(com.xxf.arch.R.id.design_bottom_sheet);
        }
        return null;
    }


    /**
     * 调用时机:oncreateView之后
     * 1.onViewCreated
     * 2.onStart
     * 3.onResume
     * 都可以
     *
     * @return
     */
    @Nullable
    public BottomSheetBehavior<FrameLayout> getBehavior() {
        FrameLayout bottomSheetView = getBottomSheetView();
        if (bottomSheetView != null) {
            return BottomSheetBehavior.from(bottomSheetView);
        }
        return null;
    }

    @Override
    public void setWindowSize(int width, int height) {
        View bottomSheet = getBottomSheetView();
        if (bottomSheet == null) {
            return;
        }
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();
        if (layoutParams.width != height || layoutParams.height != height) {
            layoutParams.height = height;
            layoutParams.width = width;
            bottomSheet.requestLayout();
        }
    }

    @Override
    public void setWindowWidth(int width) {
        View bottomSheet = getBottomSheetView();
        if (bottomSheet == null) {
            return;
        }
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();
        if (layoutParams.width != width) {
            layoutParams.width = width;
            bottomSheet.requestLayout();
        }
    }

    @Override
    public void setWindowHeight(int height) {
        View bottomSheet = getBottomSheetView();
        if (bottomSheet == null) {
            return;
        }
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();
        if (layoutParams.height != height) {
            layoutParams.height = height;
            bottomSheet.requestLayout();
        }
    }

    @Nullable
    @Override
    public Window getWindow() {
        return super.getWindow();
    }

    @Nullable
    @Override
    public FrameLayout getDecorView() {
        Window window = getWindow();
        if (window != null) {
            return (FrameLayout) window.getDecorView();
        }
        return null;
    }

    @Nullable
    @Override
    public FrameLayout getContentParent() {
        Window window = getWindow();
        if (window != null) {
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
}
