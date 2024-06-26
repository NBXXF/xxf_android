package com.xxf.arch.component

import android.os.Bundle
import android.view.View

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2018/9/7
 * Description :fragment 生命周期函数
 */
interface FragmentComponent {
    fun onViewCreated(view: View, savedInstanceState: Bundle?)
    fun onDestroyView()
}