package com.xxf.view.snackbar

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import androidx.annotation.CallSuper

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2021/9/2
 * Description ://避免业务dialog 弹出导致snackbar 显示不全
 */
class SnackBarFragment : androidx.fragment.app.DialogFragment() {

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.snackbar_dialog_fragment_style);
    }

    override fun onStart() {
        super.onStart()
        if (showsDialog) {
            val window = dialog?.window
            window?.attributes?.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
            window?.setGravity(Gravity.TOP)
            window?.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
        }
        handler.removeCallbacksAndMessages(null)
        handler.postDelayed({
            dismissAllowingStateLoss()
        }, 2000)
    }

    fun getDecorView(): View? {
        try {
            if (showsDialog) {
                return dialog?.window?.decorView
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return null;
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }


}