package com.xxf.arch.core;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

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
        this.register(application);
    }


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        super.onActivityCreated(activity, savedInstanceState);
        if (activity instanceof FragmentActivity) {
            rxLifecycleProviderMap.put((LifecycleOwner) activity, LifecycleProviderAndroidImpl.createLifecycleProvider((LifecycleOwner) activity));
            //fragment
            ((FragmentActivity) activity).getSupportFragmentManager().registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
                @Override
                public void onFragmentPreCreated(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
                    super.onFragmentPreCreated(fm, f, savedInstanceState);
                    rxLifecycleProviderMap.put(f, LifecycleProviderAndroidImpl.createLifecycleProvider(f));
                }


                @Override
                public void onFragmentDestroyed(FragmentManager fm, Fragment f) {
                    super.onFragmentDestroyed(fm, f);
                    rxLifecycleProviderMap.remove(f);
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
