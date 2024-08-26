package com.xxf.log


interface LogInterceptor {
    /**
     * @param level 日志级别 [android.util.Log.DEBUG][android.util.Log.INFO] ...
     * @return true 标识就不会再打印了
     */
    fun onIntercept(level: Int): Boolean
}