package com.xxf.arch.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.Lifecycle;
import android.support.annotation.NonNull;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.xxf.arch.lifecycle.IRxLifecycleObserver;
import com.xxf.arch.lifecycle.RxLifecycleObserver;
import com.xxf.arch.lifecycle.RxLifecycleObserverProvider;

import io.reactivex.Observable;

/**
 * @author youxuan  E-mail:youxuan@icourt.cc
 * @version 2.3.1
 * @Description 具有RxJava生命周期管理
 * @Company Beijing icourt
 * @date createTime：2018/9/7
 */
public class XXFViewModel extends AndroidViewModel
        implements
        RxLifecycleObserverProvider,
        LifecycleProvider<Lifecycle.Event> {
    private final IRxLifecycleObserver innerRxLifecycleObserver = new RxLifecycleObserver();

    public XXFViewModel(@NonNull Application application) {
        super(application);
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
}
