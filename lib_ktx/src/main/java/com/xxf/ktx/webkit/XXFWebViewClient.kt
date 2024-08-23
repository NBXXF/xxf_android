package com.xxf.ktx.webkit

import android.graphics.Bitmap
import android.net.http.SslError
import android.webkit.RenderProcessGoneDetail
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.CallSuper

/**
 * 处理常规设置
 * 1.处理ssl
 * 2.增加判断网页是不是完全加载完成
 */
open class XXFWebViewClient : WebViewClient() {

    private var mPageFinishedExhaustive = false

    @CallSuper
    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        mPageFinishedExhaustive = false
    }

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

    @CallSuper
    final override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        if (view?.progress == 100 && !this.mPageFinishedExhaustive) {
            this.mPageFinishedExhaustive = true
        }
        this.onPageFinished(view, url, this.mPageFinishedExhaustive)
    }

    /**
     * @param view
     * @param url
     * @param isPageFinishedExhaustive 是否彻底加载页面结束
     */
    open fun onPageFinished(view: WebView?, url: String?, isPageFinishedExhaustive: Boolean) {

    }
}