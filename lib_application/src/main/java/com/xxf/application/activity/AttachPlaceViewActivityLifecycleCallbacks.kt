package com.xxf.application.activity

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.FrameLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.1
 * @Description 为Activity, fragment 提供全局的占位隐藏view 常用语统计活跃时间 或者进入开发者模式
 * @date createTime：2018/9/7
 */
abstract class AttachPlaceViewActivityLifecycleCallbacks: SimpleActivityLifecycleCallbacks() {
    val TAG_PLACE_TOUCH_VIEW = "${AttachPlaceViewActivityLifecycleCallbacks::class.java.name}_touch_place_view";

    /**
     * 全局的占位view 事件只能是setOnTouchListener 不能用setOnClickListener和setOnLongClickListener
     * @return  null 代表不注册
     */
    abstract fun onCreatePlaceView(activity: Activity):View?

    /**
     * 全局的占位view 事件只能是setOnTouchListener 不能用setOnClickListener和setOnLongClickListener
     *  @return  null 代表不注册
     */
    abstract fun onCreatePlaceView(dialog: Dialog):View?

    /**
     * 用一个占位的View 来监听点击
     * dialog 需要用contentView 容器
     */
    private fun hookWindowTouch(dialog: Dialog) {
        val window: Window? = dialog.window
        window?.let {
            val findViewWithTag = window.decorView.findViewWithTag<View>(TAG_PLACE_TOUCH_VIEW)
            if (findViewWithTag == null) {
                val contentView = window.findViewById<FrameLayout>(android.R.id.content)
                onCreatePlaceView(dialog)?.let { placeView->
                    (contentView).addView(
                        placeView.apply {
                            this.tag = TAG_PLACE_TOUCH_VIEW
                            this.setBackgroundColor(Color.TRANSPARENT)
                        },
                        ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                    )
                }
            }
        }
    }

    /**
     * 用一个占位的View 来监听点击
     */
    private fun hookWindowTouch(activity: Activity) {
        val window = activity.window
        window?.let {
            val findViewWithTag = window.decorView.findViewWithTag<View>(TAG_PLACE_TOUCH_VIEW)
            if (findViewWithTag == null) {
                onCreatePlaceView(activity)?.let { placeView->
                    (window.decorView as ViewGroup).addView(
                        placeView.apply {
                            this.tag = TAG_PLACE_TOUCH_VIEW
                            this.setBackgroundColor(Color.TRANSPARENT)
                        },
                        ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                    )
                }
            }
        }
    }


    override fun onActivityPreCreated(activity: Activity, savedInstanceState: Bundle?) {
        super.onActivityPreCreated(activity, savedInstanceState)
        try {
            this.hookWindowTouch(activity);
        }catch (e:Throwable){
            e.printStackTrace()
        }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        if (activity is FragmentActivity) {
            activity.supportFragmentManager.registerFragmentLifecycleCallbacks(object :
                FragmentManager.FragmentLifecycleCallbacks() {

                override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
                    super.onFragmentResumed(fm, f)
                    if (f is DialogFragment && f.showsDialog) {
                        try {
                            f.dialog?.let { hookWindowTouch(it) }
                        } catch (e: Throwable) {
                            e.printStackTrace()
                        }
                    }
                }
            }, true)
        }
    }
}