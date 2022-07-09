package com.xxf.arch.component

import android.view.Gravity
import android.view.Window
import android.widget.FrameLayout

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2018/9/7
 * Description ://控制窗口大小
 * 各个组件控制大小方式不统一 这里统一约定api
 */
interface WindowComponent {
    /**
     * 获取创建的window
     */
    fun getWindow(): Window?

    /**
     * 获取窗口内置的 DecorView
     */
    fun getDecorView(): FrameLayout?

    /**
     * 获取窗口内置 contentView
     * 避免和activity#setContentView api冲突
     */
    fun getContentParent(): FrameLayout?

    /**
     * 设置容器大小
     * 注意各个组件类型 的默认大小不一致
     * dialog wrap_content
     * bottomsheetdialogxxx 是MATCH_PARENT (内部是全屏的window 业务需要控制根view)
     */
    fun setWindowSize(width: Int, height: Int)

    /**
     * 设置容器宽度
     * 注意各个组件类型 的默认大小不一致
     * dialog wrap_content
     * bottomsheetdialogxxx 是MATCH_PARENT (内部是全屏的window 业务需要控制根view)
     */
    fun setWindowWidth(width: Int)

    /**
     * 设置容器高度
     * 注意各个组件类型 的默认大小不一致
     * dialog wrap_content
     * bottomsheetdialogxxx 是MATCH_PARENT (内部是全屏的window 业务需要控制根view)
     */
    fun setWindowHeight(height: Int)

    /**
     * @param amount The new dim amount, from 0 for no dim to 1 for full dim.
     */
    fun setDimAmount(amount:Float)

    /**
     * 设置位置
     */
    fun setGravity(gravity: Int)
}