package com.xxf.download.m3u8.demo

import android.app.Application
import com.xxf.log.logE
import io.reactivex.rxjava3.plugins.RxJavaPlugins

class BaseApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        initXXF()
    }
    private fun initXXF(){
        //不耗时,尽可能早初始化,避免网络请求没有初始化
        RxJavaPlugins.setErrorHandler { throwable ->
            val key = "================>RxJava"
            logE(key) {
                throwable
            }

        }
    }
}