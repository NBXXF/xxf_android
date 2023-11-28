package com.xxf.activityresult

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.xxf.ktx.identityId
import java.util.concurrent.atomic.AtomicInteger

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.1
 * @Description  用新方式来 处理activityForResult和 permissionForResult
 * @date createTime：2020/9/4
 */
internal object AutoInjectActivityResultLifecycleCallbacks :
    Application.ActivityLifecycleCallbacks {
    internal class LifecycleStartActivityForResultWrapper(
        val contracts: List<StartActivityForResultContract>
    ) {
        /**
         * 控制并发
         */
        internal val localRequestCode: AtomicInteger = AtomicInteger(0);
    }

    private val cacheMap = mutableMapOf<String, LifecycleStartActivityForResultWrapper>()

    /**
     * 获取占位的contact
     */
    internal fun getPlacedContract(container: ComponentActivity): StartActivityForResultContract? {
        return cacheMap[container.identityId]?.let {
            return it.contracts[it.localRequestCode.getAndIncrement() % it.contracts.size].also {
               // println("===================>xxx:"+it)
            }
        }
    }

    /**
     * 并发数量,避免请求权限 并发与rx zip 等
     * 本身占位不影响性能 目测5个不到1ms
     */
    internal var concurrent: Int = 5

    override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
        (activity as? ComponentActivity)?.let {
            placeRegister(it)
        }
    }

    private fun placeRegister(container: ComponentActivity) {
        val batchList = mutableListOf<StartActivityForResultContract>()
        for (i in 0 until concurrent) {
            batchList.add(StartActivityForResultContract(container))
        }
        cacheMap[container.identityId] = LifecycleStartActivityForResultWrapper(batchList)
    }

    private fun placeUnRegister(container: ComponentActivity) {
        cacheMap[container.identityId]?.run {
            this.contracts.forEach {
                it.unregister()
            }
            cacheMap.remove(container.identityId)
        }
    }


    override fun onActivityStarted(activity: Activity) {

    }

    override fun onActivityResumed(activity: Activity) {

    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {
        (activity as? ComponentActivity)?.let {
            placeUnRegister(it)
        }
    }
}