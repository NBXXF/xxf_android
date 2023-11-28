package com.xxf.application.activitylifecycle

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import com.xxf.application.activityCache
import com.xxf.application.lifecycle.ViewLifecycleOwner

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description
 */
/**
 * 单例
 */
object AndroidActivityStackProvider : SimpleActivityLifecycleCallbacks(){

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        super.onActivityCreated(activity, savedInstanceState)
        activityCache.add(activity)

        /**
         * 关联lifecycle
         */
        if (activity is LifecycleOwner) {
            ViewLifecycleOwner.set(activity.window.decorView, activity)
        }
        if (activity is FragmentActivity) {
            activity.supportFragmentManager.registerFragmentLifecycleCallbacks(InnerFragmentLifecycleCallbacks, true)
        }
    }


    override fun onActivityDestroyed(activity: Activity) {
        super.onActivityDestroyed(activity)
        activityCache.remove(activity)
    }
}