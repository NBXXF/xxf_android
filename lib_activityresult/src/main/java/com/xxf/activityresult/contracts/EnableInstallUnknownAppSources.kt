package com.xxf.activityresult.contracts

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresPermission

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.1
 * @Description  允许管理安装未知应用  其他更多参考 [androidx.activity.result.contract.ActivityResultContracts]里面的静态类
 *               需要声明权限    <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES"/> 声明之后 就会在权限列表展示,这个不是动态权限
 * @date createTime：2020/9/5
 */
class EnableInstallUnknownAppSources() : AppDetailsSettingsContract() {
    override fun isSupported(context: Context?): Boolean {
        return true
    }
    @RequiresPermission(Manifest.permission.REQUEST_INSTALL_PACKAGES)
    override fun isEnabled(context: Context?): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context?.packageManager?.canRequestPackageInstalls() == true
        } else {
            true
        }
    }
}