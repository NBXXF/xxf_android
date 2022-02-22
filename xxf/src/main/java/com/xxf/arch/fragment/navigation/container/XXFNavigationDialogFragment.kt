package com.xxf.arch.fragment.navigation.container

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.xxf.arch.R
import com.xxf.arch.fragment.XXFDialogFragment
import com.xxf.arch.fragment.navigation.container.XXFBottomSheetNavigationDialogFragment.Companion.TAG_DEFAULT_NAV_HOST
import com.xxf.arch.fragment.navigation.INavigationController
import com.xxf.arch.fragment.navigation.NavController
import com.xxf.arch.fragment.navigation.NavigationOwner

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description 支持导航[左右] 类似activity 返回，解决jetpack navigation框架 不能在弹窗里面左右导航的
 * @date createTime：2022/2/22
 * 有默认布局
 * @param defaultNavHost 默认fragment
 */
open class XXFNavigationDialogFragment(var defaultNavHost: (() -> Fragment)? = null) :
    XXFDialogFragment<Unit>(R.layout.xxf_fragment_container), NavigationOwner {

    private val navController: INavigationController by lazy { NavController(this.childFragmentManager) }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : BottomSheetDialog(requireContext(), theme) {
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
            navController.navigation(this, false, TAG_DEFAULT_NAV_HOST)
        }
    }

    override fun getNavigation(): INavigationController {
        return navController
    }
}