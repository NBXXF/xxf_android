package com.xxf.arch.lifecycle;

/**
 * @author xuanyouwu@163.com
 * @version 2.3.1
 * @Description lifecycle观察
 * @date createTime：2018/9/7
 */
public interface XXFFullLifecycleObserver {

    void onCreate();

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();
}
