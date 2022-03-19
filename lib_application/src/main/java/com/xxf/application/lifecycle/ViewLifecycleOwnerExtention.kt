package com.xxf.application.lifecycle

import android.view.View
import androidx.lifecycle.LifecycleOwner

/**
 * 查找最近的LifecycleOwner
 *  解决 Android 本身的fragment 包装了 LifecycleOwner
 * 导致获取的LifecycleOwner 不能转换成fragment
 */
fun View.findViewLifecycleOwner(): LifecycleOwner? {
    return ViewLifecycleOwner.get(this)
}