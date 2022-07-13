package com.xxf.arch.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.CallSuper;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.xxf.application.lifecycle.ViewLifecycleOwner;
import com.xxf.arch.R;
import com.xxf.arch.component.BottomSheetWindowComponent;
import com.xxf.arch.component.ObservableComponent;
import com.xxf.utils.DensityUtil;
import com.xxf.utils.RAUtils;
import com.xxf.view.round.CornerUtil;

import org.jetbrains.annotations.NotNull;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;

/**
 * @version 2.3.1
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description BottomSheetDialogFragment
 * @date createTime：2018/9/7
 */

public class XXFBottomSheetDialogFragment<E>
        extends BottomSheetDialogFragment implements ObservableComponent<BottomSheetDialogFragment, E>, BottomSheetWindowComponent {
    private final String TAG_PREFIX = "show_rau_";
    @LayoutRes
    private int mContentLayoutId;
    private final Subject<Object> componentSubject = PublishSubject.create().toSerialized();

    public XXFBottomSheetDialogFragment() {

    }

    public XXFBottomSheetDialogFragment(@LayoutRes int contentLayoutId) {
        this.mContentLayoutId = contentLayoutId;
        // super(contentLayoutId);
    }

    @Override
    public Observable<Pair<BottomSheetDialogFragment, E>> getComponentObservable() {
        return componentSubject.ofType(Object.class)
                .map(new Function<Object, Pair<BottomSheetDialogFragment, E>>() {
                    @Override
                    public Pair<BottomSheetDialogFragment, E> apply(Object o) throws Throwable {
                        return Pair.create(XXFBottomSheetDialogFragment.this, (E) o);
                    }
                });
    }

    @Override
    public void setComponentResult(E result) {
        if (result != null) {
            componentSubject.onNext(result);
        }
    }

    @CallSuper
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /***
     * 禁止复写
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (this.mContentLayoutId != 0) {
            return inflater.inflate(this.mContentLayoutId, container, false);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * 会重复调用
     *
     * @param view
     * @param savedInstanceState
     */
    @CallSuper
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewLifecycleOwner.set(view, this);
        /**
         * 设置默认10dp圆角
         */
        if (getShowsDialog()) {
            View design_bottom_sheet = getBottomSheetView();
            if (design_bottom_sheet != null) {
                CornerUtil.INSTANCE.clipViewRadius(design_bottom_sheet, DensityUtil.dip2px(10));
            }
        }
    }

    /**
     * 需要调用父类的方法,否则影响XXF.startActivityForResult
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @CallSuper
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 需要调用父类的方法,否则影响XXF.requestPermission
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @CallSuper
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 会重复调用 禁止复写
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @CallSuper
    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public int show(@NonNull FragmentTransaction transaction, @Nullable String tag) {
        if (RAUtils.INSTANCE.isLegal(TAG_PREFIX + this.getClass().getName(), RAUtils.DURATION_DEFAULT)) {
            return super.show(transaction, tag);
        }
        return -1;
    }

    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        if (RAUtils.INSTANCE.isLegal(TAG_PREFIX + this.getClass().getName(), RAUtils.DURATION_DEFAULT)) {
            super.show(manager, tag);
        }
    }

    @Override
    public void showNow(@NonNull FragmentManager manager, @Nullable String tag) {
        if (RAUtils.INSTANCE.isLegal(TAG_PREFIX + this.getClass().getName(), RAUtils.DURATION_DEFAULT)) {
            super.showNow(manager, tag);
        }
    }


    @Nullable
    @Override
    public FrameLayout getBottomSheetView() {
        View dec = getDecorView();
        if (dec != null) {
            return dec.findViewById(R.id.design_bottom_sheet);
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
        return getDialog() != null ? getDialog().getWindow() : null;
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
    @Override
    public void setCanceledOnTouchOutside(boolean cancel) {
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.setCanceledOnTouchOutside(cancel);
        }
    }

}
