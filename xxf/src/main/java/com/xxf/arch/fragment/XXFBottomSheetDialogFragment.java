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

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.xxf.arch.component.ObservableComponent;

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
        extends BottomSheetDialogFragment implements ObservableComponent<BottomSheetDialogFragment, E> {

    @Deprecated
    private View contentView;
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

    @Deprecated
    public final void setContentView(@LayoutRes int layoutResID) {
        this.contentView = getLayoutInflater().inflate(layoutResID, null);
    }

    @Deprecated
    public final void setContentView(View view) {
        this.contentView = view;
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
        if (this.contentView != null) {
            ViewGroup parent = (ViewGroup) this.contentView.getParent();
            if (parent != null) {
                parent.removeView(this.contentView);
            }
            return this.contentView;
        }
        return super.onCreateView(inflater,container,savedInstanceState);
    }

    /**
     * 会重复调用
     *
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
        return null;
    }
}
