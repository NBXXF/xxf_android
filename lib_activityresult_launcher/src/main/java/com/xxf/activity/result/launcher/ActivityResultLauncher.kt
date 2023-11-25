package com.xxf.activity.result.launcher

import android.app.Application
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.1
 * @Description  用新方式来 处理activityForResult和 permissionForResult
 * @date createTime：2018/9/5
 */
object ActivityResultLauncher {

    /**
     * 初始化
     */
    fun init(application: Application) {
        application.registerActivityLifecycleCallbacks(AutoInjectActivityResultLifecycleCallbacks)
    }

}

/**
 * 获取唯一注册的activityResultLauncher
 */
private fun LifecycleOwner.activityResultLauncher(): LifecycleStartActivityForResult? {
    return AutoInjectActivityResultLifecycleCallbacks.cache.firstOrNull {
        it.lifecycleOwner == this;
    }?.lifecycleStartActivityForResult
}


/**
 *  startActivityForResult API封装 包括权限的请求方式(新的android sdk 兼容了 权限)
 *  跳转其他页面获取结果
 *  例子:
 *  this.startActivityForResult(ActivityResultContracts.RequestPermission(),Manifest.permission.CAMERA){
 *             Toast.makeText(this, "结果:$it",Toast.LENGTH_LONG).show();
 *  }
 *
 *  @param contact 参考 [androidx.activity.result.contract.ActivityResultContracts]里面的静态类
 *  @param input
 *  @param options
 *  @param activityResultCallback
 */
@JvmOverloads
fun <I,O> LifecycleOwner.startActivityForResult(
    contact: ActivityResultContract<I, O>,
    input:I,
    options: ActivityOptionsCompat?=null,
    activityResultCallback: ActivityResultCallback<O>
) {
    val context= (this as? Fragment)?.requireContext()?: (this as ComponentActivity)
    activityResultLauncher()?.launch(
        contact.createIntent(context,input),
        options
    ){
        activityResultCallback.onActivityResult(contact.parseResult(it.resultCode,it.data))
    }
}


/**
 *  startActivityForResult API封装 包括权限的请求方式(新的android sdk 兼容了 权限)
 *  跳转其他页面获取结果
 *  @param input
 *  @param options
 *  @param activityResultCallback
 */
@JvmOverloads
fun LifecycleOwner.startActivityForResult(
    input: Intent,
    options: ActivityOptionsCompat?=null,
    activityResultCallback: ActivityResultCallback<ActivityResult>
) {
  this.startActivityForResult(
      ActivityResultContracts.StartActivityForResult(),
      input,
      options,
      activityResultCallback)
}

