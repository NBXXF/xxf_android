package com.xxf.arch.utils

import android.app.AlertDialog
import android.app.Application
import android.content.Intent
import android.graphics.Color
import android.os.Process
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import androidx.annotation.MainThread
import com.xxf.application.application
import com.xxf.application.topActivity
import com.nbxxf.kpower.http.XXFHttp
import com.xxf.arch.http.databinding.XxfLayoutHostInputBinding
import com.xxf.preferences.CustomPreferencesOwner
import com.xxf.preferences.preferencesBinding
import com.xxf.ktx.selectLast
import kotlin.system.exitProcess


/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.1
 * @Description  切换域名
 * @date createTime：2018/9/7
 */
object SwitchHostUtils {
    object HostSpServiceDelegate : CustomPreferencesOwner() {
        var DEFAULT_HOST = "https://github.com/"

        /**
         * app的域名
         */
        var host: String by preferencesBinding(
            key = "_app_api_host",
            default = DEFAULT_HOST
        )
    }

    @MainThread
    fun showDialog(hostOptions: Array<String>) {
        val context = topActivity
        val hostInputBinding = XxfLayoutHostInputBinding.inflate(context.layoutInflater!!)
            .apply {
                this.inputValueSelect.setOnClickListener {
                    AlertDialog.Builder(context)
                        .setCancelable(false)
                        .setTitle("选择host".toNoThemeString())
                        .setItems(
                            hostOptions.map {
                                it.toNoThemeString()
                            }.toTypedArray()
                        ) { dialog, which ->
                            with(this.inputValueTv) {
                                setText(
                                    hostOptions[which]
                                );
                                selectLast()
                            }
                            dialog.dismiss();
                        }
                        .setNeutralButton(
                            "取消".toNoThemeString()
                        ) { dialog, which ->
                            dialog.dismiss();
                        }
                        .show()
                }
                this.inputValueTv.setText(HostSpServiceDelegate.host);
            };
        AlertDialog.Builder(context)
            .setCancelable(false)
            .setTitle("运行环境".toNoThemeString())
            .setView(hostInputBinding.root)
            .setPositiveButton(
                "确认".toNoThemeString()
            ) { dialog, which ->
                if (!TextUtils.isEmpty(hostInputBinding.inputValueTv.text)) {
                    switchHost(hostInputBinding.inputValueTv.text.trim().toString())
                }
            }.setNeutralButton(
                "取消".toNoThemeString()
            ) { dialog, which ->
                dialog.dismiss();
            }.show()
    }


    @JvmOverloads
    fun getHost(): String {
        return HostSpServiceDelegate.host
    }

    @JvmOverloads
    fun switchHost(host: String) {
        HostSpServiceDelegate.host = host
        XXFHttp.clearAllApiService()
        application.relaunchApp()
    }

    /**
     * 避免修改系统主题导致的颜色不对的情况
     */
    private fun String.toNoThemeString(): SpannableString {
        return SpannableString(this).apply {
            this.setSpan(
                ForegroundColorSpan(Color.BLUE),
                0,
                this.length,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE
            )
        }
    }

    /**
     * 重启app
     */
    private fun Application.relaunchApp() {
        val intent =
            this.packageManager.getLaunchIntentForPackage(this.packageName)
        intent!!.addFlags(
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    or Intent.FLAG_ACTIVITY_CLEAR_TASK
        )
        applicationContext.startActivity(intent)
        Process.killProcess(Process.myPid())
        exitProcess(0)
    }
}