package com.xxf.arch.dialogfragment;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
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

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.RxLifecycle;
import com.xxf.arch.annotation.BindVM;
import com.xxf.arch.annotation.BindView;
import com.xxf.arch.lifecycle.IRxLifecycleObserver;
import com.xxf.arch.lifecycle.LifecycleFunction;
import com.xxf.arch.viewmodel.XXFViewModel;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * @author xuanyouwu@163.com
 * @version 2.3.1
 * @Description
 * @date createTime：2018/9/7
 */
public class XXFDialogFragment extends DialogFragment implements IRxLifecycleObserver {
    private static final LifecycleFunction LIFECYCLEFUNCTION = new LifecycleFunction();
    private final BehaviorSubject<Lifecycle.Event> lifecycleSubject = BehaviorSubject.create();

    protected ViewDataBinding binding;
    protected XXFViewModel vm;


    @Override
    public Observable<Lifecycle.Event> lifecycle() {
        return lifecycleSubject.hide();
    }

    @Override
    public <T> LifecycleTransformer<T> bindUntilEvent(Lifecycle.Event event) {
        if (event == Lifecycle.Event.ON_ANY) {
            throw new IllegalArgumentException("event can not Lifecycle.Event.ON_ANY");
        }
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    @Override
    public <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycle.bind(lifecycleSubject, LIFECYCLEFUNCTION);
    }

    @CallSuper
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLifecycle().removeObserver(this);
        getLifecycle().addObserver(this);
        binding = DataBindingUtil.inflate(getLayoutInflater(), getClass().getAnnotation(BindView.class).value(), null, false);
        vm = ViewModelProviders.of(this).get(getClass().getAnnotation(BindVM.class).value());
        getLifecycle().removeObserver(vm);
        getLifecycle().addObserver(vm);
    }

    @Override
    public final void onBindRxLifecycle(LifecycleOwner owner, Lifecycle.Event event) {
        switch (event) {
            case ON_CREATE:
                lifecycleSubject.onNext(Lifecycle.Event.ON_CREATE);
                break;
            case ON_START:
                lifecycleSubject.onNext(Lifecycle.Event.ON_START);
                break;
            case ON_RESUME:
                lifecycleSubject.onNext(Lifecycle.Event.ON_RESUME);
                break;
            case ON_PAUSE:
                lifecycleSubject.onNext(Lifecycle.Event.ON_PAUSE);
                break;
            case ON_STOP:
                lifecycleSubject.onNext(Lifecycle.Event.ON_STOP);
                break;
            case ON_DESTROY:
                lifecycleSubject.onNext(Lifecycle.Event.ON_DESTROY);
                break;
            default:
                break;
        }
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
        getLifecycle().removeObserver(vm);
        getLifecycle().removeObserver(this);
    }
}
