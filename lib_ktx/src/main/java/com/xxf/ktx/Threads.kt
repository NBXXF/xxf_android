

@file:Suppress("unused")

package com.xxf.ktx

import android.os.Handler
import android.os.Looper

val mainThreadHandler by lazy { Handler(Looper.getMainLooper()) }

val isMainThread: Boolean get() = Looper.myLooper() == Looper.getMainLooper()

fun runMainThread(block: () -> Unit) {
  if (isMainThread) block() else mainThreadHandler.post(block)
}

fun runMainThread(delayMillis: Long, block: () -> Unit) =
  mainThreadHandler.postDelayed(block, delayMillis)
