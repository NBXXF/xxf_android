package com.xxf.activityresult.contracts

import android.content.Context
import android.content.Intent
import android.provider.Settings


/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.1
 * @Description  打开蓝牙详细设置界面打开蓝牙  其他更多参考 [androidx.activity.result.contract.ActivityResultContracts]里面的静态类
 *               需声明权限   <uses-permission android:name="android.permission.BLUETOOTH"/>
 * @date createTime：2020/9/5
 */
class EnableBluetoothSettingContract :
    EnableBluetoothContract() {
    override fun createIntent(context: Context, input: Unit): Intent {
        super.createIntent(context, input)
        return Intent(Settings.ACTION_BLUETOOTH_SETTINGS)
    }
}