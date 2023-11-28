

@file:Suppress("unused")

package com.xxf.ktx

import android.app.Application
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.res.Configuration.UI_MODE_NIGHT_MASK
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.graphics.drawable.Drawable
import androidx.core.content.pm.PackageInfoCompat

//lateinit var application: Application
//  internal set

lateinit var application: Application


inline val Application.packageInfo: PackageInfo
  get() = this.packageManager.getPackageInfo(packageName, 0)

inline val Application.appName: String
  get() =applicationInfo.loadLabel(packageManager).toString()

inline val Application.appIcon: Drawable get() = packageInfo.applicationInfo.loadIcon(packageManager)

inline val Application.appVersionName: String get() = packageInfo.versionName

inline val Application.appVersionCode: Long get() = PackageInfoCompat.getLongVersionCode(packageInfo)

inline val Application.isAppDebug: Boolean
  get() = packageManager.getApplicationInfo(packageName, 0).flags and ApplicationInfo.FLAG_DEBUGGABLE != 0

inline val Application.isAppDarkMode: Boolean
  get() = (resources.configuration.uiMode and UI_MODE_NIGHT_MASK) == UI_MODE_NIGHT_YES


