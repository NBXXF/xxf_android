package com.xxf.arch.rxjava.lifecycle.internal;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Function;


/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description
 */
public final class RxLifecycleAndroidLifecycle {

    private RxLifecycleAndroidLifecycle() {
        throw new AssertionError("No instances");
    }

    @NonNull
    @CheckResult
    public static <T> LifecycleTransformer<T> bindLifecycle(@NonNull Observable<Lifecycle.Event> lifecycle) {
        return RxLifecycle.bind(lifecycle, LIFECYCLE);
    }

    private static final Function<Lifecycle.Event, Lifecycle.Event> LIFECYCLE = new Function<Lifecycle.Event, Lifecycle.Event>() {
        @Override
        public Lifecycle.Event apply(Lifecycle.Event lastEvent) throws Exception {
            switch (lastEvent) {
                case ON_CREATE:
                    return Lifecycle.Event.ON_DESTROY;
                case ON_START:
                    return Lifecycle.Event.ON_STOP;
                case ON_RESUME:
                    return Lifecycle.Event.ON_PAUSE;
                case ON_PAUSE:
                    return Lifecycle.Event.ON_STOP;
                case ON_STOP:
                    return Lifecycle.Event.ON_DESTROY;
                case ON_DESTROY:
                    throw new OutsideLifecycleException("Cannot bind to Activity lifecycle when outside of it.");
                default:
                    throw new UnsupportedOperationException("Binding to " + lastEvent + " not yet implemented");
            }
        }
    };
}
