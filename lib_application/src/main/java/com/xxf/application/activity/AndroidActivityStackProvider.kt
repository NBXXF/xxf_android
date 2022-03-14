package com.xxf.application.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import com.xxf.application.ApplicationInitializer
import java.util.*

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description
 */
interface ActivityStackProvider {
    /**
     * 栈顶act
     */
    val topActivity: Activity?

    /**
     * 目前的根act
     */
    val rootActivity: Activity?

    /**
     * 目前栈里面所有act
     */
    val allActivity: Array<Activity>

    /**
     * 或activity的状态
     *
     * @param activity
     * @return
     */
    fun getActivityLifecycle(activity: Activity): Lifecycle.Event

    /**
     * 目前栈里是不是空
     */
    fun empty(): Boolean

    /**
     * 重启app
     */
    fun restartApp();

    /**
     * 是否在后台
     */
    fun isBackground(): Boolean;
}

/**
 * 单例
 */
object AndroidActivityStackProvider : SimpleActivityLifecycleCallbacks(), ActivityStackProvider {
    val activityStack = Stack<Activity>()

    /**
     * 并不是所有activity都是FragmentActivity,可能不是LifecyclerOwner的子类,比如sdk里面的页面
     */
    val activityLifecycle: MutableMap<Activity, Lifecycle.Event> = LinkedHashMap()
    override val topActivity: Activity?
        get() {
            try {
                return activityStack.lastElement()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }
    override val rootActivity: Activity?
        get() {
            try {
                return activityStack.firstElement()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }
    override val allActivity: Array<Activity>
        get() = activityStack.toTypedArray()

    override fun getActivityLifecycle(activity: Activity): Lifecycle.Event {
        return activityLifecycle[activity] ?: return Lifecycle.Event.ON_DESTROY
    }

    override fun empty(): Boolean {
        return activityStack.isEmpty()
    }


    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        super.onActivityCreated(activity, savedInstanceState)
        activityLifecycle[activity] = Lifecycle.Event.ON_CREATE
        activityStack.push(activity)
    }

    override fun onActivityStarted(activity: Activity) {
        super.onActivityStarted(activity)
        activityLifecycle[activity] = Lifecycle.Event.ON_START
    }

    override fun onActivityResumed(activity: Activity) {
        super.onActivityResumed(activity)
        activityLifecycle[activity] = Lifecycle.Event.ON_RESUME
    }

    override fun onActivityPaused(activity: Activity) {
        super.onActivityPaused(activity)
        activityLifecycle[activity] = Lifecycle.Event.ON_PAUSE
    }

    override fun onActivityStopped(activity: Activity) {
        super.onActivityStopped(activity)
        activityLifecycle[activity] = Lifecycle.Event.ON_STOP
    }

    override fun onActivityDestroyed(activity: Activity) {
        super.onActivityDestroyed(activity)
        activityLifecycle.remove(activity)
        activityStack.remove(activity)
    }


    /**
     * 重启app
     */
    override fun restartApp() {
        try {
            val activities = ArrayList(activityStack)
            for (activity in activities) {
                activity?.finish()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            val packageManager = ApplicationInitializer.applicationContext.packageManager ?: return
            val intent = packageManager.getLaunchIntentForPackage(ApplicationInitializer.applicationContext.packageName)
            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                ApplicationInitializer.applicationContext.startActivity(intent)
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    override fun isBackground(): Boolean {
        for ((_, event) in activityLifecycle) {
            if (event != Lifecycle.Event.ON_STOP) {
                return false
            }
        }
        return true
    }
}