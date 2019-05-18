package com.xxf.arch;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.xxf.arch.rxjava.lifecycle.LifecycleProviderAndroidImpl;
import com.xxf.arch.rxjava.lifecycle.internal.LifecycleProvider;
import com.xxf.arch.rxjava.lifecycle.internal.LifecycleTransformer;
import com.xxf.arch.utils.SimpleActivityLifecycleCallbacks;

import java.util.EmptyStackException;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 框架初始化
 */
public class XXF {
    private XXF() {

    }

    private static Application _context;
    private static final AndroidActivityStack ACTIVITY_STACK_INSTANCE = new AndroidActivityStack();
    private static final AndroidLifecycleProvider LIFECYCLE_PROVIDER_INSTANCE = new AndroidLifecycleProvider();


    public static void init(Application application) {
        if (_context == null) {
            synchronized (XXF.class) {
                if (_context == null) {
                    _context = application;
                    initActStack();
                    initLifecycleProvider();
                }
            }
        }
    }

    private static void initActStack() {
        synchronized (XXF.class) {
            _context.unregisterActivityLifecycleCallbacks(ACTIVITY_STACK_INSTANCE);
            _context.registerActivityLifecycleCallbacks(ACTIVITY_STACK_INSTANCE);
        }
    }

    private static void initLifecycleProvider() {
        synchronized (XXF.class) {
            _context.unregisterActivityLifecycleCallbacks(LIFECYCLE_PROVIDER_INSTANCE);
            _context.registerActivityLifecycleCallbacks(LIFECYCLE_PROVIDER_INSTANCE);
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
        return LIFECYCLE_PROVIDER_INSTANCE.rxLifecycleProviderMap.get(lifecycleOwner).bindUntilEvent(event);
    }

    /**
     * 绑定生命周期
     *
     * @param lifecycleOwner
     * @param <T>
     * @return
     */
    public static <T> LifecycleTransformer<T> bindToLifecycle(@NonNull LifecycleOwner lifecycleOwner) {
        return LIFECYCLE_PROVIDER_INSTANCE.rxLifecycleProviderMap.get(lifecycleOwner).bindToLifecycle();
    }


    /**
     * act stack
     */
    private static class AndroidActivityStack extends SimpleActivityLifecycleCallbacks {
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

    /**
     * act stack
     */
    private static class AndroidLifecycleProvider extends SimpleActivityLifecycleCallbacks {
        public Map<LifecycleOwner, LifecycleProvider<Lifecycle.Event>> rxLifecycleProviderMap = new ConcurrentHashMap<>();

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            super.onActivityCreated(activity, savedInstanceState);
            if (activity instanceof LifecycleOwner) {
                rxLifecycleProviderMap.put((LifecycleOwner) activity, LifecycleProviderAndroidImpl.createLifecycleProvider((LifecycleOwner) activity));
            }
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            super.onActivityDestroyed(activity);
            if (activity instanceof LifecycleOwner) {
                rxLifecycleProviderMap.remove((LifecycleOwner) activity);
            }
        }
    }

}
