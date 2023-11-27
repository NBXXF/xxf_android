package com.xxf.activityresult.contracts

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.core.app.NotificationManagerCompat
import com.xxf.activityresult.contracts.setting.SettingEnableContract


/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.1
 * @Description  打开通知设置  其他更多参考 [androidx.activity.result.contract.ActivityResultContracts]里面的静态类
 * @date createTime：2020/9/5
 */
class EnableNotificationContract : SettingEnableContract() {
    override fun createIntent(context: Context, input: Unit): Intent {
        super.createIntent(context, input)
        return Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            .putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
    }

    override fun isSupported(context: Context?): Boolean {
        return true
    }

    override fun isEnabled(context: Context?): Boolean {
        try {
            val notificationManagerCompat =
                NotificationManagerCompat.from(context!!)
            return notificationManagerCompat.areNotificationsEnabled()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

}