package com.xxf.arch.core;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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

    boolean empty();
}

public class AndroidActivityStackProvider extends SimpleActivityLifecycleCallbacks
        implements ActivityStackProvider {

    final Stack<Activity> activityStack = new Stack<>();

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
    public boolean empty() {
        return activityStack.isEmpty();
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        super.onActivityCreated(activity, savedInstanceState);
        activityStack.push(activity);
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        super.onActivityDestroyed(activity);
        activityStack.remove(activity);
    }
}
