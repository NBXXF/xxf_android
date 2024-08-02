package com.xxf.ktx.webkit

import android.webkit.WebView
import androidx.annotation.MainThread
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