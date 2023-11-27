package com.xxf.activityresult.contracts.setting

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContract
import androidx.annotation.CallSuper

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.1
 * @Description  特性功能是否支持 开关是否打开 如 Settings.ACTION_NFC_SETTINGS  更多参考[android.provider.Settings]
 * @date createTime：2020/9/3
 */
abstract class SettingEnableContract : ActivityResultContract<Unit, Boolean>() {
    protected var context: Context? = null

    /**
     * 是否支持该功能设置,比如某些设备不具备NFC模块
     */
    abstract fun isSupported(context: Context?): Boolean

    /**
     * 开关是否打开(包含判断是否支持该功能)
     */
    abstract fun isEnabled(context: Context?): Boolean

    @CallSuper
    override fun createIntent(context: Context, input: Unit): Intent {
        this.context = context
        return Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
        }
    }

    final override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
        return isEnabled(this.context)
    }
}