package com.xxf.arch.test;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import com.xxf.arch.XXF;
import com.xxf.arch.core.Logger;
import com.xxf.arch.widget.progresshud.ProgressHUD;
import com.xxf.arch.widget.progresshud.ProgressHUDFactory;
import com.xxf.view.loading.DefaultProgressHUDImpl;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * @author xuanyouwu@163.com
 * @version 2.3.1
 * @Description
 * @date createTime：2018/9/7
 */
public class BaseApplication extends Application {
    public static BaseApplication INSTANCE;

    public static BaseApplication getInstance() {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        XXF.init(this,
                new Logger() {
                    @Override
                    public boolean isLoggable() {
                        return false;
                    }

                    @Override
                    public void d(String msg) {

                    }

                    @Override
                    public void d(String msg, Throwable tr) {
                    }

                    @Override
                    public void e(String msg) {

                    }

                    @Override
                    public void e(String msg, Throwable tr) {

                    }
                },
                new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d("=============>", "t:" + throwable);
                    }
                },
                new Function<Throwable, String>() {
                    @Override
                    public String apply(Throwable throwable) throws Exception {
                        return null;
                    }
                });
        //设置默认的loading
        XXF.setProgressHUDProvider(new ProgressHUDFactory.ProgressHUDProvider() {
            @Override
            public ProgressHUD onCreateProgressHUD(Activity context) {
                return new DefaultProgressHUDImpl(context);
            }
        });

        this.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {
                Log.d("==========>act:", "" + activity);
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
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }
}
