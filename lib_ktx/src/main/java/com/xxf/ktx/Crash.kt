

@file:Suppress("unused")

package com.xxf.ktx

import android.content.Context
import android.os.Looper
import com.xxf.ktx.cacheDirPath
import com.xxf.ktx.mainThreadHandler
import com.xxf.ktx.print
import java.io.File
import java.time.Instant

inline fun handleUncaughtException(crossinline block: (Thread, Throwable) -> Unit) {
  val defaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler()
  Thread.setDefaultUncaughtExceptionHandler { t, e ->
    block(t, e)
    defaultCrashHandler?.uncaughtException(t, e)
  }
}

inline fun handleMainThreadException(crossinline block: (Throwable) -> Unit) {
  mainThreadHandler.post {
    while (true) {
      try {
        Looper.loop()
      } catch (e: Throwable) {
        block(e)
      }
    }
  }
}

fun Context.saveCrashLogLocally(dirPath: String = cacheDirPath) =
  handleUncaughtException { thread, e ->
    val now = Instant.now()
    File(dirPath, "crash_${now.format("yyyy-MM-dd")}.txt").print(append = true) {
      println("Time:          ${now.format("yyyy-MM-dd HH:mm:ss")}")
      println("App version:   $appVersionName ($appVersionCode)")
      println("OS version:    Android $sdkVersionName ($sdkVersionCode)")
      println("Manufacturer:  $deviceManufacturer")
      println("Model:         $deviceModel")
      println("Thread:        ${thread.name}")
      println()
      e.printStackTrace(this)
      println()
      println("-----------------------------------------------------")
      println()
    }
  }
