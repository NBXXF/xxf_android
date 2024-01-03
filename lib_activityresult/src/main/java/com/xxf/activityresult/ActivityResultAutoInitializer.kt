package com.xxf.activityresult

import android.app.Application
import android.content.Context
import androidx.startup.Initializer

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.1
 * @Description  自动初始化
 * @date createTime：2018/9/7
 */
internal class ActivityResultAutoInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        ActivityResultLauncher.init(context.applicationContext as Application)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}