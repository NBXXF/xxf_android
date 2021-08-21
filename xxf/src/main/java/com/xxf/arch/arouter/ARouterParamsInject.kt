package com.xxf.arch.arouter

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.alibaba.android.arouter.exception.InitException
import com.alibaba.android.arouter.launcher.ARouter
import com.xxf.application.activity.SimpleActivityLifecycleCallbacks

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description
 */
object ARouterParamsInject : SimpleActivityLifecycleCallbacks() {
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        super.onActivityCreated(activity, savedInstanceState)
        try {
            ARouter.getInstance();
            ARouter.getInstance().inject(activity)
        } catch (e: InitException) {
            e.printStackTrace();
        }
        if (activity is FragmentActivity) {
            activity.supportFragmentManager
                    .registerFragmentLifecycleCallbacks(object : FragmentManager.FragmentLifecycleCallbacks() {
                        override fun onFragmentPreCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
                            super.onFragmentPreCreated(fm, f, savedInstanceState)
                            ////自动注入ARouter
                            try {
                                ARouter.getInstance();
                                ARouter.getInstance().inject(f)
                            } catch (e: InitException) {
                                e.printStackTrace();
                            }
                        }
                    }, true)
        }
    }
}