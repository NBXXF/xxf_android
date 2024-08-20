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
    runMainThread(this.handler ?: mainThreadHandler, block)
}

fun <T : View> T.runMainThread(delayMillis: Long, block: () -> Unit) {
    runMainThread(this.handler ?: mainThreadHandler, delayMillis, block)
}


fun <T : View> T.runOnUiThread(block: () -> Unit) {
    runMainThread(this.handler ?: mainThreadHandler, block)
}

fun <T : View> T.runOnUiThread(delayMillis: Long, block: () -> Unit) {
    runMainThread(this.handler ?: mainThreadHandler, delayMillis, block)
}

/********************View 结束***************************/


/********************Activity 开始***************************/
fun <T : Activity> T.runMainThread(block: () -> Unit) {
    runMainThread(this.contentView.handler ?: mainThreadHandler, block)
}

fun <T : Activity> T.runMainThread(delayMillis: Long, block: () -> Unit) {
    runMainThread(this.contentView.handler ?: mainThreadHandler, delayMillis, block)
}


fun <T : Activity> T.runOnUiThread(block: () -> Unit) {
    runMainThread(this.contentView.handler ?: mainThreadHandler, block)
}

fun <T : Activity> T.runOnUiThread(delayMillis: Long, block: () -> Unit) {
    runMainThread(this.contentView.handler ?: mainThreadHandler, delayMillis, block)
}

/********************Activity 结束***************************/


/********************Fragment 开始***************************/
fun <T : Fragment> T.runMainThread(block: () -> Unit) {
    runMainThread(this.view?.handler ?: mainThreadHandler, block)
}

fun <T : Fragment> T.runMainThread(delayMillis: Long, block: () -> Unit) {
    runMainThread(this.view?.handler ?: mainThreadHandler, delayMillis, block)
}


fun <T : Fragment> T.runOnUiThread(block: () -> Unit) {
    runMainThread(this.view?.handler ?: mainThreadHandler, block)
}

fun <T : Fragment> T.runOnUiThread(delayMillis: Long, block: () -> Unit) {
    runMainThread(this.view?.handler ?: mainThreadHandler, delayMillis, block)
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


/**
 * 线程睡眠
 * 不使用interrupt()中断会发生什么
 * 具体而言，在以下情况下未使用 Thread.currentThread().interrupt() 方法可能发生的情况如下：
 *
 * 在普通线程中未处理中断请求：
 * 如果线程处于运行状态且没有检查中断状态，线程将继续执行，不会响应中断请求。
 * 这可能导致线程无法正确地停止或退出循环，使得应用程序无法及时响应中断请求。
 *
 * 在阻塞方法中未处理中断请求：
 * 如果线程处于阻塞状态（如调用了 sleep()、wait()、join() 等方法），并且没有捕获 InterruptedException 异常并进行相应的处理，线程将继续阻塞。
 * 这可能导致线程无法在收到中断请求时立即唤醒，并且无法及时响应中断。
 */
fun threadSleep(millis: Long, handleInterrupt: (e: InterruptedException) -> Unit = {}) {
    try {
        Thread.sleep(millis)
        // 可能会抛出 InterruptedException 的代码块
    } catch (e: InterruptedException) {
        // 处理中断异常
        handleInterrupt(e)//  eg. Thread.currentThread().interrupt() //恢复中断状态
    }
}

private const val mainThreadErrorMsg = "Must be called from main or ui thread";
fun requireMainThread() {
    require(isMainThread) {
        mainThreadErrorMsg
    }
}

fun checkMainThread() {
    check(isMainThread) {
        mainThreadErrorMsg
    }
}

fun requireUiThread() {
    require(isMainThread) {
        mainThreadErrorMsg
    }
}

fun checkUiThread() {
    check(isMainThread) {
        mainThreadErrorMsg
    }
}