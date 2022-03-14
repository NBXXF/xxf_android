package com.xxf.application

import android.app.Application
import android.content.Context
import androidx.startup.Initializer
import com.xxf.application.activity.AndroidActivityStackProvider

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.1
 * @Description  获取全局的context
 * @date createTime：2018/9/7
 */
class ApplicationInitializer : Initializer<Unit> {
    companion object {
        lateinit var applicationContext: Context
    }

    override fun create(context: Context) {
        applicationContext = context.applicationContext
        AndroidActivityStackProvider.register((context.applicationContext as Application))
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}