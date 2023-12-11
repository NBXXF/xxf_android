package com.xxf.utils

import android.os.Handler
import android.os.Looper

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080
 * Date: 2/3/21 2:36 PM
 * Description: handler utils
 */
object HandlerUtils {
    /**
     * 获取主线程的handler
     * @return
     */
    val mainHandler by lazy { Handler(Looper.getMainLooper()) }

    /**
     * 检查是否在主线程showToast
     *
     * @return
     */
    val isMainThread: Boolean
        get() = Looper.myLooper() == Looper.getMainLooper()
}