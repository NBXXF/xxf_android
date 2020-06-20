package com.xxf.arch.core;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
interface ActivityStackProvider {
    @Nullable
    @CheckResult
    Activity getTopActivity();

    @Nullable
    @CheckResult
    Activity getRootActivity();

    @NonNull
    Activity[] getAllActivity();

    /**
     * 或activity的状态
     *
     * @param activity
     * @return
     */
    Lifecycle.Event getActivityLifecycle(@NonNull Activity activity);

    boolean empty();
}

public final class AndroidActivityStackProvider extends SimpleActivityLifecycleCallbacks
        implements ActivityStackProvider {

    final Stack<Activity> activityStack = new Stack<>();
    /**
     * 并不是所有activity都是FragmentActivity,可能不是LifecyclerOwner的子类,比如sdk里面的页面
     */
    final Map<Activity, Lifecycle.Event> activityLifecycle = new LinkedHashMap<>();

    public AndroidActivityStackProvider(Application application) {
        this.register(application);
    }


    @Nullable
    @Override
    public Activity getTopActivity() {
        try {
            return activityStack.lastElement();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    @Override
    public Activity getRootActivity() {
        try {
            return activityStack.firstElement();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @NonNull
    @Override
    public Activity[] getAllActivity() {
        return activityStack.toArray(new Activity[activityStack.size()]);
    }

    @Override
    public Lifecycle.Event getActivityLifecycle(@NonNull Activity activity) {
        Lifecycle.Event event = activityLifecycle.get(activity);
        if (event == null) {
            return Lifecycle.Event.ON_DESTROY;
        }
        return event;
    }

    @Override
    public boolean empty() {
        return activityStack.isEmpty();
    }


    @Override
    public final void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        super.onActivityCreated(activity, savedInstanceState);
        activityLifecycle.put(activity, Lifecycle.Event.ON_CREATE);
        activityStack.push(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        super.onActivityStarted(activity);
        activityLifecycle.put(activity, Lifecycle.Event.ON_START);
    }

    @Override
    public void onActivityResumed(Activity activity) {
        super.onActivityResumed(activity);
        activityLifecycle.put(activity, Lifecycle.Event.ON_RESUME);
    }

    @Override
    public void onActivityPaused(Activity activity) {
        super.onActivityPaused(activity);
        activityLifecycle.put(activity, Lifecycle.Event.ON_PAUSE);
    }

    @Override
    public void onActivityStopped(Activity activity) {
        super.onActivityStopped(activity);
        activityLifecycle.put(activity, Lifecycle.Event.ON_STOP);
    }

    @Override
    public final void onActivityDestroyed(Activity activity) {
        super.onActivityDestroyed(activity);
        activityLifecycle.remove(activity);
        activityStack.remove(activity);
    }
}
