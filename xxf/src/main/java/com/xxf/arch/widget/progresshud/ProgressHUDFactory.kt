package com.xxf.arch.widget.progresshud;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description progress loading 工厂
 */
public class ProgressHUDFactory implements LifecycleObserver {
    private static final ConcurrentHashMap<LifecycleOwner, ProgressHUD> ProgressHUD_MAP = new ConcurrentHashMap<>();
    private static volatile ProgressHUDFactory INSTANCE;
    private static volatile FragmentManager.FragmentLifecycleCallbacks fragmentLifecycleCallbacks = new FragmentManager.FragmentLifecycleCallbacks() {
        @Override
        public void onFragmentDestroyed(FragmentManager fm, Fragment f) {
            super.onFragmentDestroyed(fm, f);
            removeProgressHud(f);
        }
    };

    public static ProgressHUDFactory getInstance() {
        if (INSTANCE == null) {
            synchronized (ProgressHUDFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ProgressHUDFactory();
                }
            }
        }
        return INSTANCE;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    void onEvent(LifecycleOwner owner, Lifecycle.Event event) {
        if (event == Lifecycle.Event.ON_DESTROY) {
            removeProgressHud(owner);
            owner.getLifecycle().removeObserver(this);
        }
    }


    /**
     * 移除
     *
     * @param key
     */
    private static void removeProgressHud(LifecycleOwner key) {
        ProgressHUD progressHUD = ProgressHUD_MAP.get(key);
        if (progressHUD != null && progressHUD.isShowLoading()) {
            progressHUD.dismissLoadingDialog();
        }
        ProgressHUD_MAP.remove(key);
    }


    public interface ProgressHUDProvider {
        ProgressHUD onCreateProgressHUD(LifecycleOwner lifecycleOwner);
    }

    public static ProgressHUDProvider progressHUDProvider;

    public static void setProgressHUDProvider(ProgressHUDProvider progressHUDProvider) {
        ProgressHUDFactory.progressHUDProvider = Objects.requireNonNull(progressHUDProvider);
    }

    /**
     * 获取 hud
     *
     * @param lifecycleOwner
     * @return
     */
    public ProgressHUD getProgressHUD(LifecycleOwner lifecycleOwner) {
        ProgressHUD progressHUD = ProgressHUD_MAP.get(Objects.requireNonNull(lifecycleOwner, "lifecycleOwner is null"));
        if (progressHUD == null) {
            //add Observer
            lifecycleOwner.getLifecycle().removeObserver(this);
            lifecycleOwner.getLifecycle().addObserver(this);
            progressHUD = progressHUDProvider.onCreateProgressHUD(lifecycleOwner);
            ProgressHUD_MAP.put(lifecycleOwner, progressHUD);
        }
        return progressHUD;
    }

}
