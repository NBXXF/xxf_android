package com.xxf.arch.arouter;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.xxf.arch.core.SimpleActivityLifecycleCallbacks;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class ArouterAppInject extends SimpleActivityLifecycleCallbacks {

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        super.onActivityCreated(activity, savedInstanceState);
        ARouter.getInstance().inject(activity);
        if (activity instanceof FragmentActivity) {
            FragmentActivity fragmentActivity = (FragmentActivity) activity;
            fragmentActivity.getSupportFragmentManager()
                    .registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
                        @Override
                        public void onFragmentPreCreated(@NonNull FragmentManager fm, @NonNull Fragment f, @Nullable Bundle savedInstanceState) {
                            super.onFragmentPreCreated(fm, f, savedInstanceState);
                            ////自动注入ARouter
                            ARouter.getInstance().inject(f);
                        }
                    }, true);
        }
    }
}
