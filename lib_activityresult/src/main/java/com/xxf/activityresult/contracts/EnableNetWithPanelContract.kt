package com.xxf.activityresult.contracts

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresPermission
import com.xxf.activityresult.contracts.setting.SettingEnableContract


/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.1
 * @Description  网络开关 【面板】 其他更多参考 [androidx.activity.result.contract.ActivityResultContracts]里面的静态类
 *
 *    需要声明权限
 *   <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"></uses-permission>
 * @date createTime：2020/9/5
 */
class EnableNetWithPanelContract : SettingEnableContract() {
    override fun createIntent(context: Context, input: Unit): Intent {
        super.createIntent(context, input)
        return Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY)
    }

    override fun isSupported(context: Context?): Boolean {
        return true
    }

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    override fun isEnabled(context: Context?): Boolean {
        val connectManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            connectManager.activeNetwork != null
        } else {
            connectManager.activeNetworkInfo?.isConnected == true;
        }
    }
}