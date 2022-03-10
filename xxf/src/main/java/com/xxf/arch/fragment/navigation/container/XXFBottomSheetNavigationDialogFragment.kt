package com.xxf.arch.fragment.navigation.container

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.InnerBottomSheetDialog
import com.xxf.arch.R
import com.xxf.arch.fragment.XXFBottomSheetDialogFragment
import com.xxf.arch.fragment.navigation.INavigationController
import com.xxf.arch.fragment.navigation.NavController
import com.xxf.arch.fragment.navigation.NavigationContainer

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description 支持导航[左右] 类似activity 返回，解决jetpack navigation框架 不能在弹窗里面左右导航的
 * @date createTime：2022/2/22
 * 有默认布局
 * @param defaultNavHost 默认fragment
 */
open class XXFBottomSheetNavigationDialogFragment(var defaultNavHost: (() -> Fragment)? = null) :
    XXFBottomSheetDialogFragment<Unit>(R.layout.xxf_fragment_container), NavigationContainer {
    companion object {
        const val TAG_DEFAULT_NAV_HOST = "xxf_NavHostFragment_defaultNavHost"
    }

    private val navController: INavigationController by lazy {
        NavController(
            this,
            this.childFragmentManager
        )
    }

    open inner class NavigationBottomSheetDialog : InnerBottomSheetDialog {
        constructor(context: Context) : super(context)
        constructor(context: Context, theme: Int) : super(context, theme)
        constructor(
            context: Context,
            cancelable: Boolean,
            cancelListener: DialogInterface.OnCancelListener?
        ) : super(context, cancelable, cancelListener)

        override fun getContainerId(): Int {
            return R.layout.xxf_navigation_design_bottom_sheet_dialog
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : NavigationBottomSheetDialog(requireContext(), theme) {
            override fun onBackPressed() {
                //拦截返回事件
                if (!navController.navigationUp()) {
                    super.onBackPressed()
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        defaultNavHost?.invoke()?.apply {
            navController.navigation(this, null, TAG_DEFAULT_NAV_HOST)
        }
    }

    override fun getNavigationController(): INavigationController {
        return navController
    }
}