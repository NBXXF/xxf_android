package com.xxf.download.demo

import android.app.Application
import android.text.TextUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import com.xxf.log.logE
import io.reactivex.rxjava3.functions.Function
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import java.util.concurrent.TimeUnit

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