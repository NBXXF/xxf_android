package com.xxf.application

import com.xxf.application.activity.ActivityStackProvider
import com.xxf.application.activity.AndroidActivityStackProvider

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.1
 * @Description  获取全局的context
 * @date createTime：2018/9/7
 */

/**
 * 全局上下文
 */
val applicationContext by lazy {
    ApplicationInitializer.applicationContext
}

/**
 * activity 栈
 */
val activityStack by lazy {
    AndroidActivityStackProvider as ActivityStackProvider;
}
