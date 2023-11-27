package com.xxf.activityresult.contracts

import android.Manifest
import android.bluetooth.BluetoothManager
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
 * @Description  wifi开关  其他更多参考 [androidx.activity.result.contract.ActivityResultContracts]里面的静态类
 *               需声明权限   <uses-permission android:name="android.permission.BLUETOOTH"/>
 * @date createTime：2020/9/5
 */
open class EnableWifiContract : SettingEnableContract() {
    override fun createIntent(context: Context, input: Unit): Intent {
        super.createIntent(context, input)
        return Intent(Settings.ACTION_WIFI_SETTINGS)
    }

    override fun isSupported(context: Context?): Boolean {
        return context?.getSystemService(Context.BLUETOOTH_SERVICE) != null
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