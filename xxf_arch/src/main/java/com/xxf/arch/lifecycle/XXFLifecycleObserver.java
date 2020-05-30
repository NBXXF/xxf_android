
package com.xxf.arch.lifecycle;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;

/**
 * @author xuanyouwu@163.com
 * @version 2.3.1
 * @Description lifecycle观察
 * @date createTime：2018/9/7
 */
public class XXFLifecycleObserver implements LifecycleObserver {

    private XXFFullLifecycleObserver fullLifecycleObserver;

    public XXFLifecycleObserver(XXFFullLifecycleObserver fullLifecycleObserver) {
        this.fullLifecycleObserver = fullLifecycleObserver;
    }

    public XXFLifecycleObserver() {
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    protected void onCreate() {
        if (fullLifecycleObserver != null) {
            fullLifecycleObserver.onCreate();
        }
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    protected void onStart() {
        if (fullLifecycleObserver != null) {
            fullLifecycleObserver.onCreate();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    protected void onResume() {
        if (fullLifecycleObserver != null) {
            fullLifecycleObserver.onCreate();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    protected void onPause() {
        if (fullLifecycleObserver != null) {
            fullLifecycleObserver.onCreate();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    protected void onStop() {
        if (fullLifecycleObserver != null) {
            fullLifecycleObserver.onCreate();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    protected void onDestroy() {
        if (fullLifecycleObserver != null) {
            fullLifecycleObserver.onCreate();
        }
    }

}
