
package com.xxf.arch.lifecycle;



import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.1
 * @Description lifecycle观察
 * @date createTime：2018/9/7
 * Deprecated
 * This annotation required the usage of code generation or reflection, which should be avoided. Use DefaultLifecycleObserver or LifecycleEventObserver instead.
 *
 *  DefaultLifecycleObserver  LifecycleEventObserver
 */
@Deprecated
public interface XXFLifecycleObserver extends LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    default void onCreate(){
        //Log.d("====>life","onCreate");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    default void onStart(){
        //Log.d("====>life","onStart");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    default void onResume(){
        //Log.d("====>life","onResume");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    default void onPause(){
        //Log.d("====>life","onPause");
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    default void onStop(){
        //Log.d("====>life","onStop");
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    default void onDestroy(){
        //Log.d("====>life","onDestroy");
    }
}
