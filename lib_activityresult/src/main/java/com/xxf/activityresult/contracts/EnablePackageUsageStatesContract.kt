package com.xxf.activityresult.contracts

import android.Manifest
import android.annotation.SuppressLint
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Process
import android.provider.Settings
import androidx.annotation.RequiresPermission
import com.xxf.activityresult.contracts.setting.SettingEnableContract
import com.xxf.ktx.appOpsManager


/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.1
 * @Description  打开获取应用使用情况设置  其他更多参考 [androidx.activity.result.contract.ActivityResultContracts]里面的静态类
 * @date createTime：2020/9/5
 * 知晓设备中应用的使用统计。它能给我们提供应用的进入前台动作与时间戳、进入后台的动作与时间戳、上次的使用时间、使用总时长等等信息。
 * 此功能在原生的设置-应用-使用统计中有所展示。
 * 在一般的场景下，我们可以用此管理开发一些应用使用时长统计、连续使用时长提醒、统计应用被前台化的次数/时间段从而分析应用的使用率。
 *
 *
 *     <uses-permission
 *         android:name="android.permission.PACKAGE_USAGE_STATS"
 *         tools:ignore="ProtectedPermissions"/>
 */

open class EnablePackageUsageStatesContract : SettingEnableContract() {

    @SuppressLint("ObsoleteSdkInt")
    override fun createIntent(context: Context, input: Unit): Intent {
        super.createIntent(context, input)
        return Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
        }.run {
            if (runCatching { this.resolveActivity(context.packageManager) }
                    .getOrNull() == null) {
                //非特定app
                Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
            } else {
                this
            }
        }
    }

    override fun isSupported(context: Context?): Boolean {
        return true
    }

    @RequiresPermission(
        allOf = [Manifest.permission.PACKAGE_USAGE_STATS]
    )
    override fun isEnabled(context: Context?): Boolean {
        try {
            return hasPermission(context!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    //检测用户是否对本app开启了“Apps with usage access”权限
    @RequiresPermission(
        allOf = [Manifest.permission.PACKAGE_USAGE_STATS]
    )
    private fun hasPermission(context: Context): Boolean {
        val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            context.appOpsManager!!.unsafeCheckOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                Process.myUid(), context.packageName
            )
        } else {
            context.appOpsManager!!.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                Process.myUid(), context.packageName
            )
        }
        return mode == AppOpsManager.MODE_ALLOWED
    }

}