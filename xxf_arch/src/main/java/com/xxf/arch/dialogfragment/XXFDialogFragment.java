package com.xxf.arch.dialogfragment;

import android.app.Dialog;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle3.LifecycleProvider;
import com.trello.rxlifecycle3.LifecycleTransformer;
import com.xxf.arch.annotation.BindVM;
import com.xxf.arch.annotation.BindView;
import com.xxf.arch.viewmodel.XXFViewModel;
import com.xxf.arch.widget.TouchListenDialog;

import io.reactivex.Observable;

/**
 * @author xuanyouwu@163.com
 * @version 2.3.1
 * @Description
 * @date createTime：2018/9/7
 */
public class XXFDialogFragment extends DialogFragment {
    private ViewDataBinding binding;
    private XXFViewModel vm;

    public <B extends ViewDataBinding> B getBinding() {
        return (B) binding;
    }

    public <V extends XXFViewModel> V getVm() {
        return (V) vm;
    }



    @CallSuper
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BindView bindViewAnnotation = getClass().getAnnotation(BindView.class);
        if (bindViewAnnotation != null) {
            binding = DataBindingUtil.inflate(getLayoutInflater(), bindViewAnnotation.value(), null, false);
        }

        BindVM bindVMAnnotation = getClass().getAnnotation(BindVM.class);
        if (bindVMAnnotation != null) {
            vm = ViewModelProviders.of(this).get(bindVMAnnotation.value());
        }
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
        if (binding.getRoot() != null) {
            ViewGroup parent = (ViewGroup) binding.getRoot().getParent();
            if (parent != null) {
                parent.removeView(binding.getRoot());
            }
        }
        return binding.getRoot();
    }

    /**
     * 会重复调用 禁止复写
     *
     * @param view
     * @param savedInstanceState
     */
    @Override
    public final void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
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


}
