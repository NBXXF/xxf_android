package com.xxf.arch.core;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.xxf.arch.rxjava.lifecycle.LifecycleProviderAndroidImpl;
import com.xxf.arch.rxjava.lifecycle.internal.LifecycleProvider;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
interface ILifecycleProvider {
    /**
     * @param lifecycleOwner
     * @return
     */
    LifecycleProvider<Lifecycle.Event> getLifecycleProvider(LifecycleOwner lifecycleOwner);
}

public class AndroidLifecycleProvider extends SimpleActivityLifecycleCallbacks implements ILifecycleProvider {
    final Map<LifecycleOwner, LifecycleProvider<Lifecycle.Event>> rxLifecycleProviderMap = new ConcurrentHashMap<>();

    public AndroidLifecycleProvider(Application application) {
        super(application);
    }


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        super.onActivityCreated(activity, savedInstanceState);
        if (activity instanceof FragmentActivity) {
            rxLifecycleProviderMap.put((LifecycleOwner) activity, LifecycleProviderAndroidImpl.createLifecycleProvider((LifecycleOwner) activity));
            //fragment
            ((FragmentActivity) activity).getSupportFragmentManager().registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
                @Override
                public void onFragmentCreated(@NonNull FragmentManager fm, @NonNull Fragment f, @Nullable Bundle savedInstanceState) {
                    super.onFragmentCreated(fm, f, savedInstanceState);
                    rxLifecycleProviderMap.put(f, LifecycleProviderAndroidImpl.createLifecycleProvider(f));
                }
            }, true);
        }

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        super.onActivityDestroyed(activity);
        if (activity instanceof LifecycleOwner) {
            rxLifecycleProviderMap.remove((LifecycleOwner) activity);
        }
    }

    @Override
    public LifecycleProvider<Lifecycle.Event> getLifecycleProvider(LifecycleOwner lifecycleOwner) {
        return rxLifecycleProviderMap.get(lifecycleOwner);
    }
}
