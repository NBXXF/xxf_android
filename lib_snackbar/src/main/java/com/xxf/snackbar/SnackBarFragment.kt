package com.xxf.snackbar

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.annotation.CallSuper
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.xxf.utils.FragmentUtils
import java.lang.Deprecated

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2021/9/2
 * Description ://避免业务dialog 弹出导致snackbar 显示不全
 */
class SnackBarFragment : androidx.fragment.app.DialogFragment() {

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.addFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
        dialog.window?.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        dialog.window?.setGravity(Gravity.TOP)
        return dialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.snackbar_dialog_fragment_style);
    }

    override fun onStart() {
        super.onStart()
        if (showsDialog) {
            dialog?.window?.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            handler.removeCallbacksAndMessages(null)
            handler.postDelayed({
                dismissAllowingStateLoss()
            }, 2000)
        }
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

    /**
     * 不建议使用这个,不能控制重复添加的bug
     * @param transaction
     * @param tag
     * @return
     */
    @Deprecated
    override fun show(transaction: FragmentTransaction, tag: String?): Int {
        if (this.isAdded) {
            return -1
        }
        return  super.show(transaction, tag);
    }


    override fun show(manager: FragmentManager, tag: String?) {
        FragmentUtils.removeFragment(manager,tag);
        try {
            manager.executePendingTransactions()
        } catch (throwable: Throwable) {
        }
        if (this.isAdded) {
            return
        }
        super.show(manager, tag)
    }

    override fun showNow(manager: FragmentManager, tag: String?) {
        FragmentUtils.removeFragment(manager,tag)
        try {
            manager.executePendingTransactions()
        } catch (throwable: Throwable) {
        }
        if (this.isAdded) {
            return
        }
        super.showNow(manager, tag)
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }


}