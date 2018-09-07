package com.xxf.arch.activity;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

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
public class XXFActivity extends AppCompatActivity implements IRxLifecycleObserver {
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
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    @Override
    public <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycle.bind(lifecycleSubject, LIFECYCLEFUNCTION);
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
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLifecycle().removeObserver(this);
        getLifecycle().addObserver(this);
        binding = DataBindingUtil.setContentView(this, getClass().getAnnotation(BindView.class).value());
        vm = ViewModelProviders.of(this).get(getClass().getAnnotation(BindVM.class).value());
        getLifecycle().removeObserver(vm);
        getLifecycle().addObserver(vm);
    }


    @CallSuper
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (binding != null) {
            binding.unbind();
        }
        getLifecycle().addObserver(vm);
        getLifecycle().removeObserver(this);
    }
}
