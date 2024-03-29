package com.xxf.effect.overscroll.myadapter

import android.view.View
import android.webkit.WebView
import me.everything.android.ui.overscroll.adapters.IOverScrollDecoratorAdapter

/**
 * @Description: objectBox
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2021/11/30 17:34
 */
class WebViewOverScrollDecorAdapter(val mView: WebView) :
    IOverScrollDecoratorAdapter {

    override fun getView(): View {
        return mView
    }

    override fun isInAbsoluteStart(): Boolean {
        return !mView.canScrollVertically(-1)
    }

    override fun isInAbsoluteEnd(): Boolean {
        return !mView.canScrollVertically(1)
    }
}