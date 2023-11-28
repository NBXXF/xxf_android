package com.xxf.application.initializer

import android.app.Application
import android.content.Context
import androidx.startup.Initializer
import com.xxf.application.activitylifecycle.AndroidActivityStackProvider

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.1
 * @Description  获取全局的context
 * @date createTime：2018/9/7
 */
class ApplicationInitializer : Initializer<Unit> {
    companion object {
       internal lateinit var applicationContext: Application

        fun init(app: Application): Boolean {
            return if (!this::applicationContext.isInitialized) {
                applicationContext = app
                AndroidActivityStackProvider.register(app)
                true
            } else {
                false
            }
        }
    }

    override fun create(context: Context) {
        init(context.applicationContext as Application)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}