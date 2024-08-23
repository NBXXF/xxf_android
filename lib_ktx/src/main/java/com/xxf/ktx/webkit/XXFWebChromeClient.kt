package com.xxf.ktx.webkit

import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.annotation.CallSuper

/**
 * 处理常规设置
 * 2.增加判断网页是不是完全加载完成
 */
open class XXFWebChromeClient : WebChromeClient() {
    private var mPageFinishedExhaustive = false

    @CallSuper
    @Deprecated(
        "过时了",
        replaceWith = ReplaceWith("onProgressChanged(view, newProgress, extInfo)")
    )
    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        super.onProgressChanged(view, newProgress)
        if (newProgress == view?.progress) {
            if (newProgress < 100) {
                mPageFinishedExhaustive = false
            }
            if (newProgress >= 100
                && !mPageFinishedExhaustive
            ) {
                mPageFinishedExhaustive = true
            }
        } else {
            this.onProgressChanged(view, newProgress, ExtProgressInfo(false))
        }
    }

    open fun onProgressChanged(view: WebView?, newProgress: Int, extInfo: ExtProgressInfo) {

    }

    /**
     * 加载完成的额外信息
     */
    data class ExtProgressInfo(
        /**
         * 是否彻底加载页面结束
         */
        val isPageFinishedExhaustive: Boolean
    )
}