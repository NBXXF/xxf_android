package com.xxf.ktx.webkit

import android.net.http.SslError
import android.webkit.RenderProcessGoneDetail
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient

/**
 * 处理常规设置
 */
open class XXFWebViewClient : WebViewClient() {

    override fun onRenderProcessGone(view: WebView?, detail: RenderProcessGoneDetail?): Boolean {
        //处理webView 不可使用的场景
        view?.handleRenderProcessGone(detail)

        /**
         * true 如果主机应用程序处理了进程已退出的情况，否则，如果渲染进程崩溃，应用程序将崩溃，如果渲染进程被系统杀死，应用程序将被杀死。
         */
        return true
    }

    override fun onReceivedSslError(
        view: WebView?,
        handler: SslErrorHandler?,
        error: SslError?,
    ) {
        //super.onReceivedSslError(view, handler, error)
        //忽略证书的错误继续加载页面内容，不会变成空白页面
        handler?.proceed()
    }
}