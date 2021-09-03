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
val ACTIVITY_RESULT by lazy { "ActivityResult" }

/**
 * 统一参数(一般情况一个参数)
 */
val ACTIVITY_PARAM by lazy { "ActivityParam" }

/**
 * 栈顶activity
 */
inline fun topActivity(): Activity? {
    return AndroidActivityStackProvider.topActivity
}

/**
 * 栈顶 fragmentActivity
 */
inline fun topFragmentActivity(): FragmentActivity? {
    if (AndroidActivityStackProvider.topActivity is FragmentActivity) {
        return AndroidActivityStackProvider.topActivity as FragmentActivity
    }
    return null
}

/**
 * 根activity
 */
inline fun rootActivity(): Activity? {
    return AndroidActivityStackProvider.rootActivity
}

/**
 * 所有Activity
 */
inline fun allActivity(): Array<Activity> {
    return AndroidActivityStackProvider.allActivity
}

/**
 * 重启app 仅activity
 */
inline fun restartApp() {
    AndroidActivityStackProvider.restartApp()
}

/**
 *  判断app是否在后台
 */
inline fun isAppBackground(): Boolean {
    return AndroidActivityStackProvider.isBackground()
}
