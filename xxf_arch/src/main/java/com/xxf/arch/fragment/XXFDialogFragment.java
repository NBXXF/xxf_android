package com.xxf.arch.fragment;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.CheckResult;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.xxf.arch.R;
import com.xxf.arch.annotation.BindVM;
import com.xxf.arch.annotation.BindView;
import com.xxf.arch.viewmodel.XXFViewModel;
import com.xxf.arch.widget.TouchListenDialog;
import com.xxf.arch.widget.progresshud.ProgressHUD;
import com.xxf.arch.widget.progresshud.ProgressHUDProvider;

/**
 * @author xuanyouwu@163.com
 * @version 2.3.1
 * @Description
 * @date createTime：2018/9/7
 */
public class XXFDialogFragment extends AppCompatDialogFragment implements ProgressHUDProvider {
    private ViewDataBinding binding;
    private XXFViewModel vm;

    public <B extends ViewDataBinding> B getBinding() {
        return (B) binding;
    }

    public <V extends XXFViewModel> V getVm() {
        return (V) vm;
    }

    private View contentView;


    @CallSuper
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BindView bindViewAnnotation = getClass().getAnnotation(BindView.class);
        if (bindViewAnnotation != null) {
            binding = DataBindingUtil.inflate(getLayoutInflater(), bindViewAnnotation.value(), null, false);
            setContentView(binding.getRoot());
        }

        BindVM bindVMAnnotation = getClass().getAnnotation(BindVM.class);
        if (bindVMAnnotation != null) {
            vm = ViewModelProviders.of(this).get(bindVMAnnotation.value());
        }
    }

    public void setContentView(@LayoutRes int layoutResID) {
        this.contentView = getLayoutInflater().inflate(layoutResID, null);
    }

    public void setContentView(View view) {
        this.contentView = view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
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
    public final View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
     * 会重复调用 禁止复写
     */
    @Override
    public final void onDestroyView() {
        super.onDestroyView();
    }

    @CallSuper
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (binding != null) {
            binding.unbind();
        }
    }


    @Override
    public ProgressHUD progressHUD() {
        return null;
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

}
