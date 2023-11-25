package com.core.result

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.xxf.activity.result.launcher.LifecycleStartActivityForResult

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.1
 * @Description  用新方式来 处理activityForResult和 permissionForResult
 * @date createTime：2018/9/5
 */
internal object AutoInjectActivityResultLifecycleCallbacks : Application.ActivityLifecycleCallbacks {
    internal class LifecycleStartActivityForResultWrapper(val lifecycleOwner: LifecycleOwner, val lifecycleStartActivityForResult:LifecycleStartActivityForResult);

    internal val cache= mutableSetOf<LifecycleStartActivityForResultWrapper>();
    override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
        (activity as? androidx.activity.ComponentActivity)?.let {
            LifecycleStartActivityForResult().also {
                cache.add(LifecycleStartActivityForResultWrapper(activity, it))
            }.register(it)
        }
    }


    override fun onActivityStarted(activity: Activity) {

    }

    override fun onActivityResumed(activity: Activity) {

    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {
        cache.firstOrNull {
            it.lifecycleOwner==activity
        }?.run {
             this.lifecycleStartActivityForResult.unregister()
            cache.remove(this)
        }
    }
}