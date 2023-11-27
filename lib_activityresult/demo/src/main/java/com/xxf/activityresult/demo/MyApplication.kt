package com.xxf.activityresult.demo

import android.app.Application
import com.xxf.activityresult.ActivityResultLauncher
import io.reactivex.rxjava3.plugins.RxJavaPlugins

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        RxJavaPlugins.setErrorHandler {

        }
        ActivityResultLauncher.init(this)
    }
}