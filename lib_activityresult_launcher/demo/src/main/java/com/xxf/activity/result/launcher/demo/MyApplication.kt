package com.xxf.activity.result.launcher.demo

import android.app.Application
import com.xxf.activity.result.launcher.ActivityResultLauncher

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        ActivityResultLauncher.init(this)
    }
}