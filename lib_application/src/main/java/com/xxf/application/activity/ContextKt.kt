package com.xxf.application.activity

import android.app.Activity
import android.app.ActivityManager
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Process
import androidx.lifecycle.LifecycleOwner

/**
 * 通过context 查找activity
 */
fun Context.findActivity(): Activity? {
    return findActivityByContext(this)
}

/**
 * 通过context 获取LifecycleOwner
 */
fun Context.findLifecycleOwner():LifecycleOwner?{
    return findActivityByContext(this) as? LifecycleOwner;
}

/**
 * 通过context 查找activity
 */
fun findActivityByContext(context: Context?): Activity? {
    if (context is Activity) {
        return context
    }
    return if (context is ContextWrapper) {
        findActivityByContext(context.baseContext)
    } else {
        null
    }
}

/**
 * 重启app
 */
fun Context.restartApp(){
//    val mContext=this;
//    val intent: Intent = mContext.getPackageManager()
//        .getLaunchIntentForPackage(mContext.packageName)!!
//    val restartIntent =
//        PendingIntent.getActivity(mContext, 0, intent,
//            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)
//    val mgr = mContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//    mgr[AlarmManager.RTC, System.currentTimeMillis() + 1000] =
//        restartIntent // 1秒钟后重启应用
//    android.os.Process.killProcess(android.os.Process.myPid());

    val intent =
        applicationContext.packageManager.getLaunchIntentForPackage(this.packageName)
    intent!!.addFlags(
        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                or Intent.FLAG_ACTIVITY_CLEAR_TASK
    )
    applicationContext.startActivity(intent)
    Process.killProcess(Process.myPid())
    System.exit(0)
}

/***
 * 重置应用
 */
fun Context.resetApp(){
    val am = AndroidActivityStackProvider.topActivity?.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?
    am?.clearApplicationUserData();
}
