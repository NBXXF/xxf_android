package com.xxf.activityresult.contracts

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract


/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.1
 * @Description  打开蓝牙开关  其他更多参考 [androidx.activity.result.contract.ActivityResultContracts]里面的静态类
 *               需声明权限   <uses-permission android:name="android.permission.BLUETOOTH"/>
 * @date createTime：2020/9/5
 */
class EnableBluetoothContract : ActivityResultContract<Unit, Boolean>() {
    private var context: Context? = null
    override fun createIntent(context: Context, input: Unit): Intent {
        this.context = context
        return Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
        val bluetoothAdapter =
            (context?.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
        return bluetoothAdapter?.isEnabled == true
    }
}