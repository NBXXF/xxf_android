package com.xxf.activity.result.launcher

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentManager.FragmentLifecycleCallbacks
import androidx.lifecycle.LifecycleOwner

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
        (activity as? LifecycleOwner)?.let {
            register(it)
        }
        (activity as? FragmentActivity)?.supportFragmentManager?.registerFragmentLifecycleCallbacks(object :
            FragmentLifecycleCallbacks() {
            override fun onFragmentCreated(
                fm: FragmentManager,
                f: Fragment,
                savedInstanceState: Bundle?
            ) {
                (f as? LifecycleOwner)?.let {
                    register(it)
                }
            }

            override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
                super.onFragmentDestroyed(fm, f)
                (f as? LifecycleOwner)?.let {
                    unregister(it)
                }
            }
        },true)
    }
    private fun register(owner: LifecycleOwner){
        LifecycleStartActivityForResult().also {
            cache.add(LifecycleStartActivityForResultWrapper(owner, it))
        }.register(owner)
    }

    private fun unregister(owner: LifecycleOwner){
        cache.firstOrNull {
            it.lifecycleOwner==owner
        }?.run {
            this.lifecycleStartActivityForResult.unregister()
            cache.remove(this)
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
        (activity as? LifecycleOwner)?.let {
            unregister(it)
        }
    }
}