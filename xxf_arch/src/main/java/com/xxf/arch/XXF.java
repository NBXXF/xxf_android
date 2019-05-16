package com.xxf.arch;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.trello.rxlifecycle3.LifecycleTransformer;
import com.xxf.arch.lifecycle.LifecycleProviderFactory;
import com.xxf.arch.utils.SimpleActivityLifecycleCallbacks;

import java.util.EmptyStackException;
import java.util.Stack;


/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 框架初始化
 */
public class XXF {
    private XXF() {

    }

    private static Application context;
    private static final ActivityStackCallbacks ACTIVITY_STACK_INSTANCE = new ActivityStackCallbacks();


    public static void init(Application context) {
        if (context == null) {
            synchronized (XXF.class) {
                if (context == null) {
                    XXF.context = context;
                    initActStack();
                }
            }
        }
    }

    private static void initActStack() {
        synchronized (XXF.class) {
            context.unregisterActivityLifecycleCallbacks(ACTIVITY_STACK_INSTANCE);
            context.registerActivityLifecycleCallbacks(ACTIVITY_STACK_INSTANCE);
        }
    }

    /**
     * 获取act stack
     *
     * @return
     */
    public static Stack<Activity> getActivityStack() {
        return ACTIVITY_STACK_INSTANCE.activityStack;
    }

    /**
     * 获取堆顶act
     *
     * @return
     * @throws EmptyStackException
     */
    public static Activity getTopActivity() throws EmptyStackException {
        return ACTIVITY_STACK_INSTANCE.activityStack.peek();
    }

    /**
     * 绑定生命周期
     *
     * @param lifecycleOwner
     * @param event
     * @param <T>
     * @return
     */
    public static <T> LifecycleTransformer<T> bindUntilEvent(@NonNull LifecycleOwner lifecycleOwner, @NonNull Lifecycle.Event event) {
        return LifecycleProviderFactory.lifecycle(lifecycleOwner).bindUntilEvent(event);
    }

    /**
     * 绑定生命周期
     *
     * @param lifecycleOwner
     * @param <T>
     * @return
     */
    public static <T> LifecycleTransformer<T> bindToLifecycle(@NonNull LifecycleOwner lifecycleOwner) {
        return LifecycleProviderFactory.lifecycle(lifecycleOwner).bindToLifecycle();
    }


    /**
     * act stack
     */
    private static class ActivityStackCallbacks extends SimpleActivityLifecycleCallbacks {
        public final Stack<Activity> activityStack = new Stack<>();

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

}
