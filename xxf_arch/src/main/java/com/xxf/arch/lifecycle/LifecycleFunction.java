package com.xxf.arch.lifecycle;

import android.arch.lifecycle.Lifecycle;

import com.trello.rxlifecycle2.OutsideLifecycleException;

import io.reactivex.functions.Function;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @version 2.3.0
 * @Description rxlife与lifecycle变换生命周期
 * @Company Beijing icourt
 * @date createTime：2017/12/13
 */
public class LifecycleFunction implements Function<Lifecycle.Event, Lifecycle.Event> {
    @Override
    public Lifecycle.Event apply(Lifecycle.Event event) throws Exception {
        switch (event) {
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
                throw new OutsideLifecycleException("Cannot bind to lifecycle lifecycle when outside of it.");
            default:
                throw new UnsupportedOperationException("Binding to " + event + " not yet implemented");
        }
    }
}
