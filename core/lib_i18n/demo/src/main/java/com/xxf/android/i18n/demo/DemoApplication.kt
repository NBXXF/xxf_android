package com.xxf.android.i18n.demo

import android.app.Application
import com.xxf.android.i18n.restoreCachedAppLanguage

class DemoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        restoreCachedAppLanguage()
    }
}
