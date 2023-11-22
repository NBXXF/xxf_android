package com.xxf.arch.fragment.navigation.container

import android.app.Dialog
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.xxf.arch.R
import com.xxf.arch.dialog.TouchListenDialog
import com.xxf.arch.fragment.XXFDialogFragment
import com.xxf.arch.fragment.navigation.INavigationController
import com.xxf.arch.fragment.navigation.NavigationContainer
import com.xxf.arch.fragment.navigation.container.XXFBottomSheetNavigationDialogFragment.Companion.TAG_DEFAULT_NAV_HOST
import com.xxf.arch.fragment.navigation.impl.FragmentNavController
import java.util.concurrent.Callable

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description 支持导航[左右] 类似activity 返回，解决jetpack navigation框架 不能在弹窗里面左右导航的
 * @date createTime：2022/2/22
 * 有默认布局
 * @param defaultNavHost 默认fragment
 */
open class XXFNavigationDialogFragment :
    XXFDialogFragment<Unit>, NavigationContainer {
    constructor() : super(R.layout.xxf_fragment_container)

    constructor(contentLayoutId: Int) : super(contentLayoutId)
    private var defaultNavHost: Callable<Fragment?> = Callable<Fragment?> { null }

    constructor(defaultNavHost: Callable<Fragment?>) : super(R.layout.xxf_fragment_container) {
        this.defaultNavHost = defaultNavHost
    }

    private val navController: INavigationController by lazy {
        FragmentNavController(this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
       return object : TouchListenDialog<Any?>(context, theme) {
            override fun onDialogTouchOutside(event: MotionEvent) {
                this@XXFNavigationDialogFragment.onDialogTouchOutside(event)
            }

           override fun onBackPressed() {
               //拦截返回事件
               if (!navController.navigationUp()) {
                   super.onBackPressed()
               }
           }
        };
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            val call = defaultNavHost.call()
            if (call == null) {
                dismissAllowingStateLoss()
                return
            } else {
                navController.navigation(call, null, TAG_DEFAULT_NAV_HOST)
            }
        } catch (e: Throwable) {
            e.printStackTrace()
            dismissAllowingStateLoss()
        }
    }

    override fun getNavigationController(): INavigationController {
        return navController
    }
}