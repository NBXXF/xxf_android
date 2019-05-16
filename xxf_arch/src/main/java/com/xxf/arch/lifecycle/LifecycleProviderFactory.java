package com.xxf.arch.lifecycle;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.annotation.NonNull;

import com.trello.rxlifecycle3.LifecycleProvider;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class LifecycleProviderFactory {

    private static volatile Map<LifecycleOwner, LifecycleProvider<Lifecycle.Event>> rxLifecycleProviderMap = new ConcurrentHashMap<>();
    private static volatile LifecycleObserver INSTANCE = new RxLifecycleObserver();

    private static class RxLifecycleObserver implements LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
        void onEvent(LifecycleOwner owner, Lifecycle.Event event) {
            if (event == Lifecycle.Event.ON_DESTROY) {
                owner.getLifecycle().removeObserver(this);
                rxLifecycleProviderMap.remove(owner);
            }
        }
    }

    /**
     * 获取并创建
     *
     * @param lifecycleOwner
     * @return
     */
    public static LifecycleProvider<Lifecycle.Event> lifecycle(@NonNull LifecycleOwner lifecycleOwner) {
        //添加LifecycleOwner 生命周期 监听
        lifecycleOwner.getLifecycle().removeObserver(INSTANCE);
        lifecycleOwner.getLifecycle().addObserver(INSTANCE);

        LifecycleProvider<Lifecycle.Event> eventLifecycleProvider = rxLifecycleProviderMap.get(lifecycleOwner);
        if (eventLifecycleProvider == null) {
            eventLifecycleProvider = newInstance(lifecycleOwner);
            //添加到缓存
            rxLifecycleProviderMap.put(lifecycleOwner, eventLifecycleProvider);
        }
        return eventLifecycleProvider;
    }

    /**
     * 创建
     *
     * @param lifecycleOwner
     * @return
     */
    private static LifecycleProvider<Lifecycle.Event> newInstance(@NonNull LifecycleOwner lifecycleOwner) {
        return AndroidLifecycle.createLifecycleProvider(lifecycleOwner);
    }

}
