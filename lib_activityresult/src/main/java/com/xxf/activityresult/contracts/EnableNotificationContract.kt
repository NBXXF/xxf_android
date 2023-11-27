package com.xxf.activityresult.contracts

import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat


/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.1
 * @Description  打开通知设置  其他更多参考 [androidx.activity.result.contract.ActivityResultContracts]里面的静态类
 * @date createTime：2020/9/5
 */
class EnableNotificationContract : AppDetailsSettingsContract<Boolean>() {
    fun isNotificationEnabled(context:Context): Boolean {
        try {
            val notificationManagerCompat =
                NotificationManagerCompat.from(context)
            return notificationManagerCompat.areNotificationsEnabled()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }
    override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
        return context?.let { isNotificationEnabled(it) } == true
    }

}