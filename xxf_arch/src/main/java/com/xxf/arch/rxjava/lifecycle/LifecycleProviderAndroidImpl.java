package com.xxf.arch.rxjava.lifecycle;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.annotation.CheckResult;


import com.xxf.arch.rxjava.lifecycle.internal.LifecycleProvider;
import com.xxf.arch.rxjava.lifecycle.internal.LifecycleTransformer;
import com.xxf.arch.rxjava.lifecycle.internal.RxLifecycle;
import com.xxf.arch.rxjava.lifecycle.internal.RxLifecycleAndroidLifecycle;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.subjects.BehaviorSubject;


/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public final class LifecycleProviderAndroidImpl implements LifecycleProvider<Lifecycle.Event>, LifecycleObserver {

    public static LifecycleProvider<Lifecycle.Event> createLifecycleProvider(LifecycleOwner owner) {
        return new LifecycleProviderAndroidImpl(owner);
    }

    private final BehaviorSubject<Lifecycle.Event> lifecycleSubject = BehaviorSubject.create();

    private LifecycleProviderAndroidImpl(LifecycleOwner owner) {
        owner.getLifecycle().addObserver(this);
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

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    void onEvent(LifecycleOwner owner, Lifecycle.Event event) {
        lifecycleSubject.onNext(event);
        if (event == Lifecycle.Event.ON_DESTROY) {
            owner.getLifecycle().removeObserver(this);
        }
    }
}
