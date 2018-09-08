package com.xxf.arch.dialogfragment;

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
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.xxf.arch.annotation.BindVM;
import com.xxf.arch.annotation.BindView;
import com.xxf.arch.lifecycle.IRxLifecycleObserver;
import com.xxf.arch.lifecycle.RxLifecycleObserver;
import com.xxf.arch.lifecycle.RxLifecycleObserverProvider;
import com.xxf.arch.viewmodel.XXFViewModel;

import io.reactivex.Observable;

/**
 * @author xuanyouwu@163.com
 * @version 2.3.1
 * @Description
 * @date createTime：2018/9/7
 */
public class XXFDialogFragment extends DialogFragment implements
        LifecycleProvider<Lifecycle.Event>,
        RxLifecycleObserverProvider {
    private final IRxLifecycleObserver innerRxLifecycleObserver = new RxLifecycleObserver();
    private ViewDataBinding binding;
    private XXFViewModel vm;

    public <B extends ViewDataBinding> B getBinding() {
        return (B) binding;
    }

    public <V extends XXFViewModel> V getVm() {
        return (V) vm;
    }

    @Override
    public IRxLifecycleObserver getRxLifecycleObserver() {
        return innerRxLifecycleObserver;
    }

    @Override
    public Observable<Lifecycle.Event> lifecycle() {
        return getRxLifecycleObserver().lifecycle();
    }

    @Override
    public <T> LifecycleTransformer<T> bindUntilEvent(Lifecycle.Event event) {
        return getRxLifecycleObserver().bindUntilEvent(event);
    }

    @Override
    public <T> LifecycleTransformer<T> bindToLifecycle() {
        return getRxLifecycleObserver().bindToLifecycle();
    }

    @CallSuper
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLifecycle().removeObserver(getRxLifecycleObserver());
        getLifecycle().addObserver(getRxLifecycleObserver());

        binding = DataBindingUtil.inflate(getLayoutInflater(), getClass().getAnnotation(BindView.class).value(), null, false);
        vm = ViewModelProviders.of(this).get(getClass().getAnnotation(BindVM.class).value());

        IRxLifecycleObserver vmRxLifecycleObserver = vm.getRxLifecycleObserver();
        getLifecycle().removeObserver(vmRxLifecycleObserver);
        getLifecycle().addObserver(vmRxLifecycleObserver);
    }

    @CallSuper
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (binding.getRoot() != null) {
            ViewGroup parent = (ViewGroup) binding.getRoot().getParent();
            if (parent != null) {
                parent.removeView(binding.getRoot());
            }
        }
        return binding.getRoot();
    }

    @CallSuper
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (binding != null) {
            binding.unbind();
        }
        if (vm != null) {
            IRxLifecycleObserver vmRxLifecycleObserver = vm.getRxLifecycleObserver();
            getLifecycle().removeObserver(vmRxLifecycleObserver);
        }
        getLifecycle().removeObserver(getRxLifecycleObserver());
    }


}
