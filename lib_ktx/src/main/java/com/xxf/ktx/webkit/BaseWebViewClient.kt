package com.xxf.ktx.webkit

import android.net.http.SslError
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient

/**
 * 处理常规设置
 */
open class BaseWebViewClient : WebViewClient() {
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