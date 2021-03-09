
package com.xxf.arch.lifecycle;


import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
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
