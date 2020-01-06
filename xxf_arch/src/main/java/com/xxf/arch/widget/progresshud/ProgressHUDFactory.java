package com.xxf.arch.widget.progresshud;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.xxf.arch.XXF;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class ProgressHUDFactory {
    private static final ConcurrentHashMap<Activity, ProgressHUD> ProgressHUD_MAP = new ConcurrentHashMap<>();
    private static final Application.ActivityLifecycleCallbacks l = new Application.ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            //移除
            ProgressHUD progressHUD = ProgressHUD_MAP.get(activity);
            if (progressHUD != null && progressHUD.isShowLoading()) {
                progressHUD.dismissLoadingDialog();
            }
            ProgressHUD_MAP.remove(activity);
        }
    };


    public interface ProgressHUDProvider {
        ProgressHUD onCreateProgressHUD(Activity context);
    }

    public static ProgressHUDProvider progressHUDProvider;

    public static void setProgressHUDProvider(ProgressHUDProvider progressHUDProvider) {
        Application application = Objects.requireNonNull(XXF.getApplication(), "must call function XXF.setProgressHUDProvider()");
        ProgressHUDFactory.progressHUDProvider = Objects.requireNonNull(progressHUDProvider);
        application.unregisterActivityLifecycleCallbacks(l);
        application.registerActivityLifecycleCallbacks(l);
    }

    /**
     * 获取 hud
     *
     * @param activity
     * @return
     */
    public static ProgressHUD getProgressHUD(Activity activity) {
        ProgressHUD progressHUD = ProgressHUD_MAP.get(activity);
        if (progressHUD == null) {
            progressHUD = progressHUDProvider.onCreateProgressHUD(activity);
            ProgressHUD_MAP.put(activity, progressHUD);
        }
        return progressHUD;
    }
}
