package com.xxf.arch.component

import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2018/9/7
 * Description ://控制窗口大小
 * 各个组件控制大小方式不统一 这里统一约定api
 */
interface BottomSheetWindowComponent : WindowComponent {

    fun getBottomSheetView(): FrameLayout?

    fun getBehavior(): BottomSheetBehavior<FrameLayout>?
}