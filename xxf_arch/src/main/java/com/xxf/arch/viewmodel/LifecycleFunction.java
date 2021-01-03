package com.xxf.arch.viewmodel;


import com.xxf.arch.rxjava.lifecycle.internal.OutsideLifecycleException;

import io.reactivex.rxjava3.functions.Function;

/**
 * @Description: java类作用描述
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/6/25 9:48
 */
public class LifecycleFunction implements Function<Event, Event> {
    public static LifecycleFunction INSTANCE=new LifecycleFunction();
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
}
