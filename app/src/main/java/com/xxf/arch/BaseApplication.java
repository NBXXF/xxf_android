package com.xxf.arch;

import android.app.Application;

import com.xxf.arch.utils.XXFAppLifecycleLogger;

/**
 * @author xuanyouwu@163.com
 * @version 2.3.1
 * @Description
 * @date createTime：2018/9/7
 */
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //注册activity和fragment生命周期
        XXFAppLifecycleLogger.register(
                this,
                new XXFAppLifecycleLogger.XXFAppLifecycleLoggerBuilder()
                        .setLogCreatedActivity(true)//在activity创建的时候打印Log
                        .setLogActivityIntent(true)//打印Intent参数
                        .setToastCreatedActivity(true)//在activity创建的时候Toast
                        .setLogCreatedFragment(true)//在fragment创建的时候打印Log
                        .setLogFragmentAragments(true)//打印fragment参数
                        .setToastCreatedFragment(true)//在fragment创建的时候Toast
                        .build());

    }
}
