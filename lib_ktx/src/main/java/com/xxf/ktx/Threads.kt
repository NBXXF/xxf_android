@file:Suppress("unused")

package com.xxf.ktx

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment

val mainThreadHandler by lazy { Handler(Looper.getMainLooper()) }

val isMainThread: Boolean get() = Looper.myLooper() == Looper.getMainLooper()

fun runMainThread(block: () -> Unit) {
    runMainThread(mainThreadHandler, block)
}

fun runOnUiThread(block: () -> Unit) {
    runMainThread(mainThreadHandler, block)
}

fun runMainThread(delayMillis: Long, block: () -> Unit) {
    runMainThread(mainThreadHandler, delayMillis, block)
}

fun runOnUiThread(delayMillis: Long, block: () -> Unit) {
    runMainThread(mainThreadHandler, delayMillis, block)
}


/********************View 开始***************************/
fun <T : View> T.runMainThread(block: () -> Unit) {
    runMainThread(this.handler?: mainThreadHandler, block)
}

fun <T : View> T.runMainThread(delayMillis: Long, block: () -> Unit) {
    runMainThread(this.handler?: mainThreadHandler, delayMillis, block)
}


fun <T : View> T.runOnUiThread(block: () -> Unit) {
    runMainThread(this.handler?: mainThreadHandler, block)
}

fun <T : View> T.runOnUiThread(delayMillis: Long, block: () -> Unit) {
    runMainThread(this.handler?: mainThreadHandler, delayMillis, block)
}

/********************View 结束***************************/


/********************Activity 开始***************************/
fun <T : Activity> T.runMainThread(block: () -> Unit) {
    runMainThread(this.contentView.handler?: mainThreadHandler, block)
}

fun <T : Activity> T.runMainThread(delayMillis: Long, block: () -> Unit) {
    runMainThread(this.contentView.handler?: mainThreadHandler, delayMillis, block)
}


fun <T : Activity> T.runOnUiThread(block: () -> Unit) {
    runMainThread(this.contentView.handler?: mainThreadHandler, block)
}

fun <T : Activity> T.runOnUiThread(delayMillis: Long, block: () -> Unit) {
    runMainThread(this.contentView.handler?: mainThreadHandler, delayMillis, block)
}

/********************Activity 结束***************************/


/********************Fragment 开始***************************/
fun <T : Fragment> T.runMainThread(block: () -> Unit) {
    runMainThread(this.view?.handler?: mainThreadHandler, block)
}

fun <T : Fragment> T.runMainThread(delayMillis: Long, block: () -> Unit) {
    runMainThread(this.view?.handler?: mainThreadHandler, delayMillis, block)
}


fun <T : Fragment> T.runOnUiThread(block: () -> Unit) {
    runMainThread(this.view?.handler?: mainThreadHandler, block)
}

fun <T : Fragment> T.runOnUiThread(delayMillis: Long, block: () -> Unit) {
    runMainThread(this.view?.handler?: mainThreadHandler, delayMillis, block)
}

/********************Fragment 结束***************************/


private fun runMainThread(handler: Handler = mainThreadHandler, block: () -> Unit) {
    if (isMainThread) block() else handler.post(block)
}

private fun runMainThread(
    handler: Handler = mainThreadHandler,
    delayMillis: Long,
    block: () -> Unit
) =
    handler.postDelayed(block, delayMillis)
