package com.xxf.arch.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * @author xuanyouwu@163.com
 * @version 2.3.1
 * @Description 为Activity, fragment查找提供帮助
 * @date createTime：2018/9/7
 */
public class SimpleActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks{
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

    }
}
