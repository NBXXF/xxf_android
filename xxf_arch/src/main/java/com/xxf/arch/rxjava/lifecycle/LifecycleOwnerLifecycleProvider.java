package com.xxf.arch.rxjava.lifecycle;

import androidx.annotation.CallSuper;
import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import com.xxf.arch.lifecycle.LifecycleOwnerProvider;
import com.xxf.arch.lifecycle.XXFFullLifecycleObserver;
import com.xxf.arch.lifecycle.XXFFullLifecycleObserverAdapter;
import com.xxf.arch.rxjava.lifecycle.internal.LifecycleProvider;
import com.xxf.arch.rxjava.lifecycle.internal.LifecycleTransformer;
import com.xxf.arch.rxjava.lifecycle.internal.RxLifecycle;
import com.xxf.arch.rxjava.lifecycle.internal.RxLifecycleAndroidLifecycle;

import java.util.Objects;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;


/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description
 */
class LifecycleOwnerLifecycleProvider implements LifecycleOwnerProvider, LifecycleProvider<Lifecycle.Event>, XXFFullLifecycleObserver {

    final BehaviorSubject<Lifecycle.Event> lifecycleSubject = BehaviorSubject.create();
    LifecycleOwner owner;

    LifecycleOwnerLifecycleProvider(@NonNull LifecycleOwner owner) {
        Objects.requireNonNull(owner);
        this.owner = owner;
        owner.getLifecycle().addObserver(new XXFFullLifecycleObserverAdapter(this));
    }

    @NonNull
    @Override
    @CheckResult
    public Observable<Lifecycle.Event> lifecycle() {
        return lifecycleSubject.hide();
    }

    @NonNull
    @Override
    @CheckResult
    public <T> LifecycleTransformer<T> bindUntilEvent(@NonNull Lifecycle.Event event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    @NonNull
    @Override
    @CheckResult
    public <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycleAndroidLifecycle.bindLifecycle(lifecycleSubject);
    }

    @CallSuper
    @Override
    public void onCreate() {
        lifecycleSubject.onNext(Lifecycle.Event.ON_CREATE);
    }

    @CallSuper
    @Override
    public void onStart() {
        lifecycleSubject.onNext(Lifecycle.Event.ON_START);
    }

    @CallSuper
    @Override
    public void onResume() {
        lifecycleSubject.onNext(Lifecycle.Event.ON_RESUME);
    }

    @CallSuper
    @Override
    public void onPause() {
        lifecycleSubject.onNext(Lifecycle.Event.ON_PAUSE);
    }

    @CallSuper
    @Override
    public void onStop() {
        lifecycleSubject.onNext(Lifecycle.Event.ON_STOP);
    }

    @CallSuper
    @Override
    public void onDestroy() {
        lifecycleSubject.onNext(Lifecycle.Event.ON_DESTROY);
    }

    @Override
    public LifecycleOwner getLifecycleOwner() {
        return this.owner;
    }
}
