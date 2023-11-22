package com.xxf.application

import android.app.Application
import com.xxf.application.activity.ActivityStackProvider
import com.xxf.application.activity.AndroidActivityStackProvider
import com.xxf.application.initializer.ApplicationInitializer

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.1
 * @Description  获取全局的context
 * @date createTime：2018/9/7
 */

/**
 * 全局上下文 Application
 */
val applicationContext: Application by lazy {
    ApplicationInitializer.applicationContext
}

