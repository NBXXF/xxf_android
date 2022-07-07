package com.xxf.application.activity

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

/**
 * 通过context 查找activity
 */
fun Context.findActivity(): Activity? {
    return findActivityByContext(this)
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
