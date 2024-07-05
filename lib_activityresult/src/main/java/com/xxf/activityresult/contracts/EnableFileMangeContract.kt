package com.xxf.activityresult.contracts

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import com.xxf.activityresult.contracts.setting.SettingEnableContract


/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.1
 * @Description  打开文件管理  其他更多参考 [androidx.activity.result.contract.ActivityResultContracts]里面的静态类
 *               需声明权限
 *               <uses-permission android:name="android.permission.MANAGE_EXTERNAL_STORAGE"/>
 *               <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
 *               <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
 * @date createTime：2020/9/5
 */
open class EnableFileMangeContract : SettingEnableContract() {
    override fun createIntent(context: Context, input: Unit): Intent {
        super.createIntent(context, input)
        return Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
    }

    override fun isSupported(context: Context?): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.R
    }


    @RequiresPermission(
        allOf = [Manifest.permission.MANAGE_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE]
    )
    override fun isEnabled(context: Context?): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            context?.let {
                ContextCompat.checkSelfPermission(
                    it, Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            } == PackageManager.PERMISSION_GRANTED
        }
    }
}