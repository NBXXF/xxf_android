

@file:Suppress("unused")

package com.xxf.ktx

import android.os.Build

inline val isXiaomiRom: Boolean get() = isRomOf("xiaomi")

inline val isHuaweiRom: Boolean get() = isRomOf("huawei")

inline val isOppoRom: Boolean get() = isRomOf("oppo")

inline val isVivoRom: Boolean get() = isRomOf("vivo")

inline val isOnePlusRom: Boolean get() = isRomOf("oneplus")

inline val isSmartisanRom: Boolean get() = isRomOf("smartisan", "deltainno")

inline val isMeiZuRom: Boolean get() = isRomOf("meizu")

inline val isSamsungRom: Boolean get() = isRomOf("samsung")

inline val isGoogleRom: Boolean get() = isRomOf("google")

inline val isSonyRom: Boolean get() = isRomOf("sony")

fun isRomOf(vararg names: String): Boolean =
  names.any { it.contains(Build.BRAND, true) || it.contains(Build.MANUFACTURER, true) }

val isHarmonyOS: Boolean
  get() {
    try {
      val clazz = Class.forName("com.huawei.system.BuildEx")
      val classLoader = clazz.classLoader
      if (classLoader != null && classLoader.parent == null) {
        return clazz.getMethod("getOsBrand").invoke(clazz) == "harmony"
      }
    } catch (e: ClassNotFoundException) {
    } catch (e: NoSuchMethodException) {
    } catch (e: Exception) {
    }
    return false
  }
