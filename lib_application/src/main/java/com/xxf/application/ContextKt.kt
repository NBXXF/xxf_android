package com.xxf.application

import android.app.Activity
import android.app.ActivityManager
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
private fun findActivityByContext(context: Context?): Activity? {
    if (context is Activity) {
        return context
    }
    return if (context is ContextWrapper) {
        findActivityByContext(context.baseContext)
    } else {
        null
    }
}

