package com.xxf.activity.result.launcher.demo

import android.app.Application
import com.xxf.activity.result.launcher.ActivityResultLauncher
import io.reactivex.rxjava3.plugins.RxJavaPlugins

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        RxJavaPlugins.setErrorHandler {

        }
        ActivityResultLauncher.init(this)
    }
}