package com.xxf.ktx.webkit

import android.app.Application
import android.os.Build
import android.webkit.WebView
import androidx.annotation.MainThread
import com.xxf.ktx.app
import com.xxf.ktx.removeFromParentView

/**
 * 安全释放
 * 并解决内存泄露
 */
@MainThread
fun <T : WebView> T.release() {
    this.removeFromParentView()
    stopLoading()            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
    getSettings().javaScriptEnabled = false;
    removeAllViews()
    destroy()
}

/**
 * 1、代码一定是在进程初始化的时候调用，比如Application中进行调用。
 * 2、代码需要在其他的三方SDK初始化之前就要调用。
 *
 * Android 9.0及以上版本，多进程使用WebView会引发应用程序崩溃。
 *
 * 官方说明：在Android 9.0中，为改善应用稳定性和数据完整性，应用无法再让多个进程共享一个WebView数据目录。
 * 如果您的应用必须在多个进程中使用WebView实例，
 * 则您必须先使WebView.setDataDirectorySuffix()方法为每个进程指定唯一的数据目录后缀，
 * 然后再在相应进程中使用WebView的给定实例。
 * 该方法会将每个进程的网络数据放入应用数据目录内其自己的目录中。
 */
fun <T : Application> T.initWebViewDataDirectoryWithAndroidP() {
    /**
     *  1、代码一定是在进程初始化的时候调用，比如Application中进行调用。
     *  2、代码需要在其他的三方SDK初始化之前就要调用。
     */
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        val processName = Application.getProcessName()
        if (this.packageName != processName) {
            WebView.setDataDirectorySuffix(processName)
        }
    }
}