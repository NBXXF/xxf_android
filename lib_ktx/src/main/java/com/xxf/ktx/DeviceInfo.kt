

package com.xxf.ktx

import android.os.Build

inline val sdkVersionName: String get() = Build.VERSION.RELEASE

inline val sdkVersionCode: Int get() = Build.VERSION.SDK_INT

inline val deviceManufacturer: String get() = Build.MANUFACTURER

inline val deviceModel: String get() = Build.MODEL
