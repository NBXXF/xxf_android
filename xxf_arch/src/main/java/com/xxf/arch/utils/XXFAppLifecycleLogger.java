package com.xxf.arch.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.FragmentLifecycleCallbacks;
import android.util.Log;
import android.widget.Toast;

/**
 * @author xuanyouwu@163.com
 * @version 2.3.1
 * @Description 为Activity, fragment查找提供帮助
 * @date createTime：2018/9/7
 */
public class XXFAppLifecycleLogger extends SimpleActivityLifecycleCallbacks {

    public static void register(Application application, XXFAppLifecycleLogger logger) {
        application.unregisterActivityLifecycleCallbacks(logger);
        application.registerActivityLifecycleCallbacks(logger);
    }

    public interface XXFLifecycleLogInterceptor {

        void onActivityCreated(XXFAppLifecycleLoggerBuilder builder, Activity activity, @Nullable Intent intent);

        void onFragmentCreated(XXFAppLifecycleLoggerBuilder builder, Fragment fragment, Bundle argments);
    }

    public static class XXFAppLifecycleLoggerBuilder {
        //默认true
        private boolean logCreatedActivity = true;
        private boolean toastCreatedActivity;
        //默认true
        private boolean logActivityIntent = true;

        //默认true
        private boolean logCreatedFragment = true;
        private boolean toastCreatedFragment;
        //默认true
        private boolean logFragmentAragments = true;
        private XXFLifecycleLogInterceptor interceptor;

        public boolean isLogCreatedActivity() {
            return logCreatedActivity;
        }

        public XXFAppLifecycleLoggerBuilder setLogCreatedActivity(boolean logCreatedActivity) {
            this.logCreatedActivity = logCreatedActivity;
            return this;
        }

        public boolean isToastCreatedActivity() {
            return toastCreatedActivity;
        }

        public XXFAppLifecycleLoggerBuilder setToastCreatedActivity(boolean toastCreatedActivity) {
            this.toastCreatedActivity = toastCreatedActivity;
            return this;
        }

        public boolean isLogActivityIntent() {
            return logActivityIntent;
        }

        public XXFAppLifecycleLoggerBuilder setLogActivityIntent(boolean logActivityIntent) {
            this.logActivityIntent = logActivityIntent;
            return this;
        }

        public boolean isLogCreatedFragment() {
            return logCreatedFragment;
        }

        public XXFAppLifecycleLoggerBuilder setLogCreatedFragment(boolean logCreatedFragment) {
            this.logCreatedFragment = logCreatedFragment;
            return this;
        }

        public boolean isToastCreatedFragment() {
            return toastCreatedFragment;
        }

        public XXFAppLifecycleLoggerBuilder setToastCreatedFragment(boolean toastCreatedFragment) {
            this.toastCreatedFragment = toastCreatedFragment;
            return this;
        }

        public boolean isLogFragmentAragments() {
            return logFragmentAragments;
        }

        public XXFAppLifecycleLoggerBuilder setLogFragmentAragments(boolean logFragmentAragments) {
            this.logFragmentAragments = logFragmentAragments;
            return this;
        }

        public XXFLifecycleLogInterceptor getInterceptor() {
            return interceptor;
        }

        public XXFAppLifecycleLoggerBuilder setInterceptor(XXFLifecycleLogInterceptor interceptor) {
            this.interceptor = interceptor;
            return this;
        }

        public XXFAppLifecycleLogger build() {
            return new XXFAppLifecycleLogger(this);
        }
    }

    private XXFAppLifecycleLoggerBuilder builder;
    private final XXFLifecycleLogInterceptor defaultInterceptor = new XXFLifecycleLogInterceptor() {
        private void log(String log) {
            Log.d("XXFAppLifecycle", log);
        }

        private String parseBundle(Bundle extras) {
            StringBuilder logBuilder = new StringBuilder();
            if (extras != null) {
                for (String key : extras.keySet()) {
                    logBuilder
                            .append("\n")
                            .append(key)
                            .append(" : ")
                            .append(extras.get(key));
                }
            }
            return logBuilder.toString();
        }

        @Override
        public void onActivityCreated(XXFAppLifecycleLoggerBuilder builder, Activity activity, @Nullable Intent intent) {
            if (builder.toastCreatedActivity) {
                Toast.makeText(activity, activity.getClass().getName(), Toast.LENGTH_SHORT).show();
            }
            StringBuilder logBuilder = new StringBuilder("=========onActivityCreated:" + activity.getClass().getName());
            if (builder.logActivityIntent && intent != null) {
                logBuilder.append(parseBundle(intent.getExtras()));
            }
            if (builder.logCreatedActivity) {
                log(logBuilder.toString());
            }
            if (builder.interceptor != null) {
                builder.interceptor.onActivityCreated(builder, activity, intent);
            }
        }

        @Override
        public void onFragmentCreated(XXFAppLifecycleLoggerBuilder builder, Fragment fragment, Bundle argments) {
            if (builder.toastCreatedFragment) {
                Toast.makeText(fragment.getContext(), fragment.getClass().getName(), Toast.LENGTH_SHORT).show();
            }
            StringBuilder logBuilder = new StringBuilder("=========onFragmentCreated:" + fragment.getClass().getName());
            if (builder.logFragmentAragments) {
                logBuilder.append(parseBundle(argments));
            }
            if (builder.logCreatedFragment) {
                log(logBuilder.toString());
            }
            if (builder.interceptor != null) {
                builder.interceptor.onFragmentCreated(builder, fragment, argments);
            }
        }
    };

    private XXFAppLifecycleLogger(XXFAppLifecycleLoggerBuilder builder) {
        this.builder = builder;
    }


    private final FragmentLifecycleCallbacks CB = new FragmentLifecycleCallbacks() {
        @Override
        public void onFragmentCreated(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
            super.onFragmentCreated(fm, f, savedInstanceState);
            defaultInterceptor.onFragmentCreated(builder, f, f.getArguments());
        }
    };

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        defaultInterceptor.onActivityCreated(builder, activity, activity.getIntent());
        if (activity instanceof FragmentActivity) {
            FragmentActivity fragmentActivity = (FragmentActivity) activity;
            if (fragmentActivity.getSupportFragmentManager() != null) {
                fragmentActivity.getSupportFragmentManager().registerFragmentLifecycleCallbacks(CB, true);
            }
        }
    }
}
