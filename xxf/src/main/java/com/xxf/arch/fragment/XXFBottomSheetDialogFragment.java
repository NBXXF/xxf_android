package com.xxf.arch.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

import androidx.annotation.CallSuper;
import androidx.annotation.CheckResult;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.bottomsheet.InnerBottomSheetDialog;
import com.xxf.application.lifecycle.ViewLifecycleOwner;
import com.xxf.arch.R;
import com.xxf.arch.component.BottomSheetComponent;
import com.xxf.arch.component.ContainerComponent;
import com.xxf.arch.component.ObservableComponent;
import com.xxf.arch.dialog.WindowExtentionKtKt;
import com.xxf.utils.DensityUtil;
import com.xxf.utils.RAUtils;
import com.xxf.view.round.CornerUtil;

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
        extends BottomSheetDialogFragment implements ObservableComponent<BottomSheetDialogFragment, E>, BottomSheetComponent {
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
            View dialogDecorView = getDialogDecorView();
            if (dialogDecorView != null) {
                View design_bottom_sheet = dialogDecorView.findViewById(R.id.design_bottom_sheet);
                if (design_bottom_sheet != null) {
                    CornerUtil.INSTANCE.clipViewRadius(design_bottom_sheet, DensityUtil.dip2px(10));
                }
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


    /**
     * 获取dialog的窗体
     *
     * @return
     */
    @CheckResult
    @Nullable
    public Window getDialogWidow() {
        return getDialog() != null ? getDialog().getWindow() : null;
    }

    /**
     * 获取dialog的DecorView
     *
     * @return
     */
    @CheckResult
    @Nullable
    public View getDialogDecorView() {
        Window dialogWidow = getDialogWidow();
        return dialogWidow != null ? dialogWidow.getDecorView() : null;
    }

    /**
     * 设置边距
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public void setDialogPadding(int left, int top, int right, int bottom) {
        View dialogDecorView = getDialogDecorView();
        if (dialogDecorView != null) {
            dialogDecorView.setPadding(left, top, right, bottom);
        }
    }

    /**
     * 设置对话框的位置
     *
     * @param gravity
     */
    public void setDialogGravity(int gravity) {
        Window dialogWidow = getDialogWidow();
        if (dialogWidow != null) {
            dialogWidow.setGravity(gravity);
        }
    }

    /**
     * 设置动画
     *
     * @param resId
     */
    public void setDialogAnimations(@StyleRes int resId) {
        Window dialogWidow = getDialogWidow();
        if (dialogWidow != null) {
            dialogWidow.setWindowAnimations(resId);
        }
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
        if (getDialog() instanceof BottomSheetDialog) {
            return ((BottomSheetDialog) getDialog()).getBehavior();
        }
        if (getDialog() instanceof InnerBottomSheetDialog) {
            return ((InnerBottomSheetDialog) getDialog()).getBehavior();
        }
        //避免复写dialog
        View dec = getDialogDecorView();
        if (dec != null) {
            View bottomSheetInternal = dec.findViewById(R.id.design_bottom_sheet);
            if (bottomSheetInternal != null && bottomSheetInternal instanceof FrameLayout) {
                return BottomSheetBehavior.from((FrameLayout) bottomSheetInternal);
            }
        }
        return null;
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

    @Override
    public void onStart() {
        super.onStart();
        if (getShowsDialog()) {
            WindowExtentionKtKt.runAlphaDimAnimation(getDialogWidow());
        }
    }


    @Nullable
    @Override
    public FrameLayout getBottomSheetView() {
        View dec = getDialogDecorView();
        if (dec != null) {
            return dec.findViewById(R.id.design_bottom_sheet);
        }
        return null;
    }

    @Override
    public void setSize(int width, int height) {
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
    public void setWidth(int width) {
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
    public void setHeight(int height) {
        View bottomSheet = getBottomSheetView();
        if (bottomSheet == null) {
            return;
        }
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();
        if (layoutParams.height != height) {
            layoutParams.width = height;
            bottomSheet.requestLayout();
        }
    }
}
