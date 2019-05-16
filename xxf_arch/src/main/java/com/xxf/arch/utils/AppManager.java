package com.xxf.arch.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * @author xuanyouwu
 * @email xuanyouwu@163.com
 * @time 2015-04-20 18:38
 */

/**
 * use new {@link com.xxf.arch.XXF}
 */
@Deprecated
public class AppManager {
    private final SimpleActivityLifecycleCallbacks activityLifecycleCallbacks = new SimpleActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            super.onActivityCreated(activity, savedInstanceState);
            addActivity(activity);
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            super.onActivityDestroyed(activity);
            removeActivity(activity);
        }
    };

    private static Stack<Activity> activityStack;
    private static AppManager instance;

    private AppManager() {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
    }

    /**
     * 单一实例
     */
    public static AppManager getAppManager() {
        if (instance == null) {
            synchronized (AppManager.class) {
                instance = new AppManager();
            }
        }
        return instance;
    }

    /**
     * 注册
     *
     * @param application
     */
    public void register(Application application) {
        application.unregisterActivityLifecycleCallbacks(activityLifecycleCallbacks);
        application.registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
    }

    public Stack<Activity> getAllActivity() {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        return activityStack;
    }

    /**
     * 添加Activity到堆栈
     */
    private void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    private void removeActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.remove(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() throws NoSuchElementException {
        if (activityStack == null) return null;
        return activityStack.lastElement();
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null && !activity.isFinishing()) {
            activity.finish();
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
                break;
            }
        }
    }

    /**
     * 结束所有Activity ,不包含指定的activity
     */
    public void finishAllActivity(Activity currentActivity) {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i) && currentActivity != activityStack.get(i)) {
                finishActivity(activityStack.get(i));
            }
        }
        activityStack.clear();
        activityStack.add(currentActivity);
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        if (activityStack == null) {
            return;
        }

        for (int i = activityStack.size() - 1; i >= 0; i--) {
            if (null != activityStack.get(i)) {
                finishActivity(activityStack.get(i));
            }
        }
        activityStack.clear();
    }

    /**
     * 获取指定的Activity
     */
    public <T extends Activity> T getActivity(Class<T> cls) {
        if (activityStack != null)
            for (Activity activity : activityStack) {
                if (activity.getClass().equals(cls)) {
                    return (T) activity;
                }
            }
        return null;
    }

    /**
     * 结束除了最底部一个以外的所有Activity
     */
    public void finishUpActivity() {
        int size = activityStack.size();
        for (int i = 0; i < size - 1; i++) {
            activityStack.pop().finish();
        }
    }

    /**
     * 退出应用程序
     */
    public void AppExit() {
        try {
            finishAllActivity();
            // 杀死该应用进程
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}