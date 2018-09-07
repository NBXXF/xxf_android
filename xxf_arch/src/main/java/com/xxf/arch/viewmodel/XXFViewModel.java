package com.xxf.arch.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.NonNull;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.RxLifecycle;
import com.xxf.arch.lifecycle.IRxLifecycleObserver;
import com.xxf.arch.lifecycle.LifecycleFunction;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * @author youxuan  E-mail:youxuan@icourt.cc
 * @version 2.3.1
 * @Description 具有RxJava生命周期管理
 * @Company Beijing icourt
 * @date createTime：2018/9/7
 */
public class XXFViewModel extends AndroidViewModel
        implements IRxLifecycleObserver {
    private static final LifecycleFunction LIFECYCLEFUNCTION = new LifecycleFunction();
    private final BehaviorSubject<Lifecycle.Event> lifecycleSubject = BehaviorSubject.create();

    public XXFViewModel(@NonNull Application application) {
        super(application);
    }


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
}
