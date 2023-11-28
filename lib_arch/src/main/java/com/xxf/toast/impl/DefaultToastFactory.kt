package com.xxf.toast.impl

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import com.xxf.arch.R
import com.xxf.toast.LimitToast
import com.xxf.toast.ToastFactory
import com.xxf.toast.ToastType
import com.xxf.snackbar.Snackbar
import com.xxf.snackbar.Snackbar.Companion.make
import com.xxf.toast.fixBadTokenException
import com.xxf.utils.DensityUtil.dip2px
import com.xxf.utils.dp

open class DefaultToastFactory : ToastFactory {
    override fun createToast(
        msg: CharSequence,
        type: ToastType,
        context: Context,
        flag: Int
    ): LimitToast {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.xxf_toast_layout, null)
        val text = view.findViewById<TextView>(android.R.id.message)
        val dp19 = dip2px(19f)
        when (type) {
            ToastType.ERROR -> {
                val errorDrawable =
                    AppCompatResources.getDrawable(view.context, R.drawable.xxf_ic_cancel_20)
                errorDrawable?.setBounds(0, 0, dp19, dp19)
                text.setCompoundDrawables(errorDrawable, null, null, null)
            }

            ToastType.NORMAL -> text.setCompoundDrawables(null, null, null, null)
            ToastType.SUCCESS -> {
                val successDrawable =
                    AppCompatResources.getDrawable(view.context, R.drawable.xxf_ic_table_checked_20)
                successDrawable?.setBounds(0, 0, dp19, dp19)
                text.setCompoundDrawables(successDrawable, null, null, null)
            }
        }
        text.text = msg
        val toast = LimitToast(context)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = view
        return toast.apply {
            //切记一定要执行
            this.fixBadTokenException()
        }
    }

    override fun createSnackbar(
        rootView: View,
        msg: CharSequence,
        type: ToastType,
        context: Context,
        flag: Int
    ): Snackbar {
        val snackbar = make(rootView, msg, Snackbar.LENGTH_SHORT)
        val snackbarView = snackbar.view
        snackbarView.setPadding(10.dp, 0, 10.dp, 0)
        snackbarView.setBackgroundColor(-0xcccccd)
        val textView =
            snackbarView.findViewById<View>(com.google.android.material.R.id.snackbar_text) as TextView
        textView.setTextColor(Color.WHITE)
        textView.maxLines = 3
        textView.compoundDrawablePadding = dip2px(7f)
        val dp19 = dip2px(19f)
        when (type) {
            ToastType.ERROR -> {
                val errorDrawable =
                    AppCompatResources.getDrawable(rootView.context, R.drawable.xxf_ic_cancel_20)
                errorDrawable?.setBounds(0, 0, dp19, dp19)
                textView.setCompoundDrawables(errorDrawable, null, null, null)
            }

            ToastType.NORMAL -> textView.setCompoundDrawables(null, null, null, null)
            ToastType.SUCCESS -> {
                val successDrawable = AppCompatResources.getDrawable(
                    rootView.context,
                    R.drawable.xxf_ic_table_checked_20
                )
                successDrawable?.setBounds(0, 0, dp19, dp19)
                textView.setCompoundDrawables(successDrawable, null, null, null)
            }
        }
        return snackbar
    }

    companion object {
        /**
         * 获取状态栏高度
         *
         * @param context context
         * @return 状态栏高度
         */
        private fun getStatusBarHeight(context: Context): Int {
            // 获得状态栏高度
            val resourceId =
                context.resources.getIdentifier("status_bar_height", "dimen", "android")
            return context.resources.getDimensionPixelSize(resourceId)
        }
    }
}