package com.xxf.activityresult.contracts

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.provider.Settings
import androidx.core.content.getSystemService
import com.xxf.activityresult.contracts.setting.SettingEnableContract

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.1
 * @Description  打开定位服务  其他更多参考 [androidx.activity.result.contract.ActivityResultContracts]里面的静态类
 * @date createTime：2020/9/5
 */
class EnableLocationContract :SettingEnableContract() {
    override fun createIntent(context: Context, input: Unit): Intent {
        super.createIntent(context, input)
        return Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
    }

    override fun isSupported(context: Context?): Boolean {
        return context?.getSystemService(Context.LOCATION_SERVICE) != null
    }

    override fun isEnabled(context: Context?): Boolean {
        return try {
            context?.getSystemService<LocationManager>()?.isProviderEnabled(LocationManager.GPS_PROVIDER) == true
        } catch (e: Exception) {
            false
        }
    }

}