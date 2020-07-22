package com.xxf.arch.rxjava.lifecycle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import com.xxf.arch.rxjava.lifecycle.internal.LifecycleProvider;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static androidx.lifecycle.Lifecycle.State.DESTROYED;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */

public final class LifecycleProviderFactory {
    final static Map<LifecycleOwner, LifecycleProviderWarpper> rxLifecycleProviderMap = new ConcurrentHashMap<>();

    static final class LifecycleProviderWarpper extends LifecycleOwnerLifecycleProvider {
        public LifecycleProviderWarpper(LifecycleOwner owner) {
            super(owner);
        }

        @Override
        public void onCreate() {
            super.onCreate();
            try {
                rxLifecycleProviderMap.put(getLifecycleOwner(), this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            try {
                rxLifecycleProviderMap.remove(getLifecycleOwner());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 处理生命周期
     *
     * @param lifecycleOwner
     * @return
     */
    @NonNull
    public static LifecycleProvider<Lifecycle.Event> getLifecycleProvider(@Nullable LifecycleOwner lifecycleOwner) {
        if (lifecycleOwner == null
                || lifecycleOwner.getLifecycle() == null
                || lifecycleOwner.getLifecycle().getCurrentState() == DESTROYED) {
            return DestoryLifecycleProvider.INSTANCE;
        }
        LifecycleProviderWarpper lifecycleProvider = rxLifecycleProviderMap.get(lifecycleOwner);
        if (lifecycleProvider == null) {
            rxLifecycleProviderMap.put(lifecycleOwner, lifecycleProvider = new LifecycleProviderWarpper(lifecycleOwner));
        }
        return lifecycleProvider;
    }
}
