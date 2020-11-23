package com.xxf.arch.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.CallSuper;
import androidx.annotation.CheckResult;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.xxf.arch.activity.ActivityForKeyProvider;
import com.xxf.arch.dialog.TouchListenDialog;

/**
 * @author xuanyouwu@163.com
 * @version 2.3.1
 * @Description
 * @date createTime：2018/9/7
 */
public class XXFDialogFragment extends AppCompatDialogFragment implements ActivityForKeyProvider,IShotFragment {

    private View contentView;

    @CallSuper
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public final void setContentView(@LayoutRes int layoutResID) {
        this.contentView = getLayoutInflater().inflate(layoutResID, null);
    }

    public final void setContentView(View view) {
        this.contentView = view;
    }

    @NonNull
    @Override
    public final Dialog onCreateDialog(Bundle savedInstanceState) {
        return new TouchListenDialog(getContext(), getTheme()) {
            @Override
            protected void onDialogTouchOutside(MotionEvent event) {
                XXFDialogFragment.this.onDialogTouchOutside(event);
            }
        };
    }

    /**
     * 外部点击
     */
    protected void onDialogTouchOutside(MotionEvent event) {
    }

    /**
     * 会重复调用 禁止复写
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public  View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (this.contentView != null) {
            ViewGroup parent = (ViewGroup) this.contentView.getParent();
            if (parent != null) {
                parent.removeView(this.contentView);
            }
        }
        return this.contentView;
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

    @Override
    public Bitmap shotFragment(@Nullable Bundle shotArgs) {
        return null;
    }
}
