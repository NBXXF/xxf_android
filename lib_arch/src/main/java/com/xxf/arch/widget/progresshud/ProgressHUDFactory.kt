package com.xxf.arch.widget.progresshud

import androidx.lifecycle.*
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description progress loading 工厂
 */
object ProgressHUDFactory : LifecycleEventObserver {
    private val ProgressHUD_MAP = ConcurrentHashMap<LifecycleOwner, ProgressHUD?>()
    var progressHUDProvider: ProgressHUDProvider = object : ProgressHUDProvider {
        override fun onCreateProgressHUD(lifecycleOwner: LifecycleOwner): ProgressHUD {
            return ProgressHUD.EMPTY;
        }
    };

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_DESTROY) {
            removeProgressHud(source)
        }
    }

    interface ProgressHUDProvider {
        fun onCreateProgressHUD(lifecycleOwner: LifecycleOwner): ProgressHUD
    }

    /**
     * 获取 hud
     *
     * @param lifecycleOwner
     * @return
     */
    fun getProgressHUD(lifecycleOwner: LifecycleOwner): ProgressHUD {
        var progressHUD: ProgressHUD? = ProgressHUD_MAP[Objects.requireNonNull(lifecycleOwner, "lifecycleOwner is null")]
        if (progressHUD == null) {
            //add Observer
            lifecycleOwner.lifecycle.removeObserver(this)
            lifecycleOwner.lifecycle.addObserver(this)
            progressHUD = progressHUDProvider!!.onCreateProgressHUD(lifecycleOwner)
            ProgressHUD_MAP[lifecycleOwner] = progressHUD!!
        }
        return progressHUD
    }

    /**
     * 移除
     *
     * @param key
     */
    private fun removeProgressHud(key: LifecycleOwner) {
        val progressHUD:ProgressHUD? = ProgressHUD_MAP[key]
        if (progressHUD != null && progressHUD.isShowLoading) {
            progressHUD.dismissLoadingDialog()
        }
        ProgressHUD_MAP.remove(key)
        key.lifecycle.removeObserver(this)
    }
}