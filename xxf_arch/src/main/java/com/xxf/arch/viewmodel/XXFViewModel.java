package com.xxf.arch.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;


import com.trello.rxlifecycle3.LifecycleTransformer;
import com.trello.rxlifecycle3.OutsideLifecycleException;
import com.trello.rxlifecycle3.RxLifecycle;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.subjects.BehaviorSubject;

/**
 * @author youxuan  E-mail:youxuan@icourt.cc
 * @version 2.3.1
 * @Description 具有RxJava生命周期管理
 * @Company Beijing icourt
 * @date createTime：2018/9/7
 */
enum Event {
    /**
     * Constant for onCreate  ViewModel.
     */
    ON_CREATE,
    /**
     * Constant for clear viewModel.
     */
    ON_CLEARED,
}

public class XXFViewModel extends AndroidViewModel {
    private static final Function LIFECYCLEFUNCTION = new Function<Event, Event>() {
        @Override
        public Event apply(Event event) throws Exception {
            switch (event) {
                case ON_CREATE:
                    return Event.ON_CLEARED;
                case ON_CLEARED:
                    throw new OutsideLifecycleException("Cannot bind to lifecycle lifecycle when outside of it.");
                default:
                    throw new UnsupportedOperationException("Binding to " + event + " not yet implemented");
            }
        }
    };
    private final BehaviorSubject<Event> lifecycleSubject = BehaviorSubject.create();

    public XXFViewModel(@NonNull Application application) {
        super(application);
        lifecycleSubject.onNext(Event.ON_CREATE);
    }

    public Observable<Event> lifecycle() {
        return lifecycleSubject.hide();
    }

    /**
     * 绑定生命周期
     *
     * @param <T>
     * @return
     */
    public <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycle.bind(lifecycleSubject, LIFECYCLEFUNCTION);
    }

    @CallSuper
    @Override
    protected void onCleared() {
        super.onCleared();
        lifecycleSubject.onNext(Event.ON_CLEARED);
    }
}
