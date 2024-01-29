package com.xxf.activityresult.contracts

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import com.xxf.activityresult.contracts.setting.SettingEnableContract
import com.xxf.ktx.locationManager

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.1
 * @Description  打开定位服务  其他更多参考 [androidx.activity.result.contract.ActivityResultContracts]里面的静态类
 * @date createTime：2020/9/5
 */
class EnableLocationContract : SettingEnableContract() {
    override fun createIntent(context: Context, input: Unit): Intent {
        super.createIntent(context, input)
        return Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
    }

    override fun isSupported(context: Context?): Boolean {
        return context?.getSystemService(Context.LOCATION_SERVICE) != null
    }


    /**
     * 修复定制平板 有开关 但是没有gps模块
     */
    private fun isLocationSettingsEnabled(context: Context): Boolean {
        try {
            var locationMode = 0
            val locationProviders: String
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                locationMode = try {
                    Settings.Secure.getInt(context.contentResolver, Settings.Secure.LOCATION_MODE)
                } catch (e: Settings.SettingNotFoundException) {
                    e.printStackTrace()
                    return false
                }
                locationMode != Settings.Secure.LOCATION_MODE_OFF
            } else {
                locationProviders = Settings.Secure.getString(
                    context.contentResolver, Settings.Secure.LOCATION_PROVIDERS_ALLOWED
                )
                locationProviders.isNotEmpty()
            }
        } catch (e: Throwable) {
            e.printStackTrace()
            return false
        }
    }

    override fun isEnabled(context: Context?): Boolean {
        return try {
            //判断GPS是否开启，GPS或者GPS开启一个就认为是开启的
            return if (context!!.locationManager!!.run {
                    // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
                    // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
                    isProviderEnabled(LocationManager.GPS_PROVIDER) || isProviderEnabled(
                        LocationManager.NETWORK_PROVIDER
                    )
                }) {
                true
            } else isLocationSettingsEnabled(context)
        } catch (e: Exception) {
            false
        }
    }

}