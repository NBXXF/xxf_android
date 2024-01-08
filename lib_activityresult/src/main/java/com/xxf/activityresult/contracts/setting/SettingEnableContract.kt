package com.xxf.activityresult.contracts.setting

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContract
import androidx.annotation.CallSuper
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.LifecycleOwner
import com.xxf.activityresult.ActivityResultContractObservable
import com.xxf.activityresult.startActivityForResult
import com.xxf.ktx.findActivity
import io.reactivex.rxjava3.core.Observable

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
        val lif: LifecycleOwner;
        lif.startActivityForResult(this)
    }

    final override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
        return isEnabled(this.context)
    }
}

/**
 *  force 是否强制再对应的页面 默认false
 */
@JvmOverloads
fun <T : SettingEnableContract> LifecycleOwner.startActivityForResult(
    contact: T,
    options: ActivityOptionsCompat? = null,
    force: Boolean
): Observable<Boolean> {
    if (force) {
        return ActivityResultContractObservable<Unit, Boolean>(this, contact, Unit, options)
    } else if (contact.isEnabled(this.findActivity())) {
        return Observable.just(true)
    }
    return ActivityResultContractObservable<Unit, Boolean>(this, contact, Unit, options)
}
