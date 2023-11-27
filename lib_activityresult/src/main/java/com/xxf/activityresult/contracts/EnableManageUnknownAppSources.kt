package com.xxf.activityresult.contracts

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import com.xxf.activityresult.contracts.setting.SettingEnableContract

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.1
 * @Description  允许管理安装未知应用 建议用[com.xxf.activity.result.contracts.EnableInstallUnknownAppSources] 其他更多参考 [androidx.activity.result.contract.ActivityResultContracts]里面的静态类
 * @date createTime：2020/9/5
 */
@Deprecated(message = "这个设置 会导致返回页面逻辑不对,建议用 [com.xxf.activity.result.contracts.EnableInstallUnknownAppSources]")
class EnableManageUnknownAppSources :
    SettingEnableContract() {


    override fun createIntent(context: Context, input: Unit): Intent {
        super.createIntent(context, input)
        return Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES).apply {
            this.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }

    override fun isSupported(context: Context?): Boolean {
        return true
    }

    override fun isEnabled(context: Context?): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context?.packageManager?.canRequestPackageInstalls() == true
        } else {
            true
        }
    }
}