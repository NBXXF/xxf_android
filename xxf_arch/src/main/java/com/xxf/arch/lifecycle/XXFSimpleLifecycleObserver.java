
package com.xxf.arch.lifecycle;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;

/**
 * @author xuanyouwu@163.com
 * @version 2.3.1
 * @Description lifecycle观察
 * @date createTime：2018/9/7
 */
public class XXFSimpleLifecycleObserver implements LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    protected final void onEvent(LifecycleOwner owner, Lifecycle.Event event) {

    }
}
