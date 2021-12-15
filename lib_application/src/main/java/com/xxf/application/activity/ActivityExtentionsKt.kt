package com.xxf.application.activity

import android.app.Activity
import androidx.fragment.app.FragmentActivity

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：8/8/21
 * Description :activity拓展类
 */
/**
 * 统一返回结果(一般情况只有一个返回值)
 */
val KEY_ACTIVITY_RESULT by lazy { "ActivityResult" }

/**
 * 统一参数(一般情况一个参数)
 */
val KEY_COMPAT_PARAM by lazy { "CompatParam" }


/**
 * 栈顶activity
 */

val topActivity: Activity?
    get() {
        return AndroidActivityStackProvider.topActivity
    }

/**
 * 栈顶 fragmentActivity
 */
val topFragmentActivity: FragmentActivity?
    get() {
        if (AndroidActivityStackProvider.topActivity is FragmentActivity) {
            return AndroidActivityStackProvider.topActivity as FragmentActivity
        }
        return null
    }

/**
 * 根activity
 */
val rootActivity: Activity?
    get() {
        return AndroidActivityStackProvider.rootActivity
    }

/**
 * 所有Activity
 */
val allActivity: Array<Activity>
    get() {
        return AndroidActivityStackProvider.allActivity
    }

/**
 * 重启app 仅activity
 */
fun restartApp() {
    AndroidActivityStackProvider.restartApp()
}

/**
 *  判断app是否在后台
 */
val isAppBackground: Boolean
    get() {
        return AndroidActivityStackProvider.isBackground()
    }
