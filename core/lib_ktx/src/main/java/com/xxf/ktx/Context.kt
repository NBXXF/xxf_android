package com.xxf.ktx

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.lifecycle.LifecycleOwner

/**
 * 通过context 获取LifecycleOwner
 */
fun Context.findLifecycleOwner(): LifecycleOwner? {
    return this.findActivity() as? LifecycleOwner;
}


/**
 * 通过context 查找activity
 */
fun Context.findActivity(): Activity? {
    var activity: Activity? = null
    var context: Context? = this
    while (context is ContextWrapper) {
        if (context is Activity) {
            activity = context
            break
        }
        context = context.baseContext
    }
    return activity
}

