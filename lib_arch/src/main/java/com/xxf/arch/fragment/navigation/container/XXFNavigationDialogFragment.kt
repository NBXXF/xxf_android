package com.xxf.arch.fragment.navigation.container

import android.app.Dialog
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.xxf.arch.R
import com.xxf.arch.dialog.TouchListenDialog
import com.xxf.arch.fragment.XXFDialogFragment
import com.xxf.arch.fragment.navigation.navigate
import java.util.concurrent.Callable

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description 支持导航[左右] 类似activity 返回，解决jetpack navigation框架 不能在弹窗里面左右导航的
 * @date createTime：2022/2/22
 * 有默认布局
 * @param defaultNavHost 默认fragment
 */
open class XXFNavigationDialogFragment :
    XXFDialogFragment<Unit> {
    constructor() : super(R.layout.xxf_fragment_container)

    constructor(contentLayoutId: Int) : super(contentLayoutId)

    private var defaultNavHost: Callable<Class<Fragment>?> = Callable<Class<Fragment>?> { null }

    constructor(defaultNavHost: Callable<Class<Fragment>?>) : super(R.layout.xxf_fragment_container) {
        this.defaultNavHost = defaultNavHost
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : TouchListenDialog<Any?>(context, theme) {
            override fun onDialogTouchOutside(event: MotionEvent) {
                this@XXFNavigationDialogFragment.onDialogTouchOutside(event)
            }

            override fun onBackPressed() {
                //拦截返回事件
                if (findNavController()?.navigateUp() != true) {
                    super.onBackPressed()
                }
            }
        };
    }

    fun Fragment.findNavController(): NavController? {
        return (childFragmentManager.findFragmentById(R.id.navigation_fragment_container) as NavHostFragment).navController
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            val call = defaultNavHost.call()
            if (call == null) {
                dismissAllowingStateLoss()
                return
            } else {
                findNavController()?.navigate(call)
            }
        } catch (e: Throwable) {
            e.printStackTrace()
            dismissAllowingStateLoss()
        }
    }

}