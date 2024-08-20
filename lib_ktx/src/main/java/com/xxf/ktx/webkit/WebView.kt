package com.xxf.ktx.webkit

import android.app.Application
import android.os.Build
import android.webkit.RenderProcessGoneDetail
import android.webkit.WebView
import androidx.annotation.MainThread
import com.xxf.ktx.app
import com.xxf.ktx.findActivity
import com.xxf.ktx.isUnavailable
import com.xxf.ktx.removeFromParentView
import com.xxf.ktx.requireMainThread

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
 * 当 Render 进程长时间无响应的时候，就会触发这个方法。比如因为 JavaScript 长时间无响应、输入相应事件长时间无响应抑或是导航至新的 url 无响应等。
 * 到这里，基本就比较明晰了。也就是说，当 WebView 的任务过重，如 JavaScrip 长时间无响应，抑或是其他导致响应事件过长的情况，
 * 都有可能会触发 render 进程移除，如果不加以处理，就会导致我们的应用进程被强制停止。
 *
 * 在android.webkit.WebViewClient.onRenderProcessGone中调用
 * 默认实现activity重启解决,当然还有局部替换的方法
 *  // 仅对我们自己的 webview 做处理
 *                 if ( view == mWebView) {
 *                   // 获取 webview 所在父布局
 *                     ViewGroup parent = (ViewGroup) mWebView.getParent();
 *                     ViewGroup.LayoutParams params = mWebView.getLayoutParams();
 *                     // 把无效不可用的 webview 从布局中移除
 *                     destroyWebView();
 *                    // 重新创建新的 webview
 *                     WebView newWebView = new WebView(getActivity());
 *                     newWebView.setId(R.id.webView);
 *                     parent.addView(newWebView, 0, params);
 *                     Bundle bundle = getArguments();
 *                     // 重走初始化流程，渲染 UI，加载 url
 *                     initView(root);
 *                     return true;
 *                 }
 *
 */
@MainThread
fun <T : WebView> T.handleRenderProcessGone(detail: RenderProcessGoneDetail?): Boolean {
    val context = this.context
    val findActivity = context.findActivity()
    if (findActivity != null && !findActivity.isUnavailable()) {
        requireMainThread()
        findActivity.recreate()
        return true
    } else {
        return false
    }
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
