package com.xxf.activity.result.launcher

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner


/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.1
 * @Description  用新方式来 处理activityForResult和 permissionForResult
 * @date createTime：2018/9/5
 */
internal class LifecycleStartActivityForResult: ActivityResultContract<Intent, ActivityResult>(),ActivityResultCallback<ActivityResult>{

    private var activityResultLauncher:ActivityResultLauncher<Intent>?=null
    private var activityResultCallback: ActivityResultCallback<ActivityResult>? = null


    override fun createIntent(context: Context, input: Intent): Intent {
        return input
    }

    fun register(owner: LifecycleOwner){
        this.activityResultLauncher = (owner as? Fragment)?.registerForActivityResult(this,this)
            ?: (owner as? androidx.activity.ComponentActivity)?.registerForActivityResult(this,this)
    }

    fun unregister(){
        activityResultLauncher?.unregister()
    }

    override fun parseResult(
        resultCode: Int, intent: Intent?
    ): ActivityResult {
        return ActivityResult(resultCode, intent)
    }

    /**
     * 启动
     */
    @JvmOverloads
    fun launch(input: Intent,
               options: ActivityOptionsCompat?=null,
               activityResultCallback: ActivityResultCallback<ActivityResult>) {
        this.activityResultCallback = activityResultCallback
        activityResultLauncher?.launch(input,options)
    }

    override fun onActivityResult(result: ActivityResult) {
        activityResultCallback?.onActivityResult(result)
    }
}