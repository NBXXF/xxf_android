package com.xxf.application

import android.app.Activity
import android.content.Context
import androidx.fragment.app.FragmentActivity
import java.util.LinkedList

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


internal val activityCache = LinkedList<Activity>()

val activityList: List<Activity> get() = activityCache.toList()

val topActivity: Activity get() = activityCache.last()

val topActivityOrNull: Activity? get() = activityCache.lastOrNull()

val topFragmentActivityOrNull: FragmentActivity? get() = topActivityOrNull as? FragmentActivity

val topActivityOrApplication: Context get() = topActivityOrNull ?: application
