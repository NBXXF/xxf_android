package com.xxf.arch.component

import android.view.Window

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2018/9/7
 * Description ://控制窗口大小
 */
interface WindowComponent {
    /**
     * 获取创建
     */
    fun getWindow(): Window?

    /**
     * 设置容器大小
     */
    fun setWindowSize(width: Int, height: Int)

    /**
     * 设置容器宽度
     */
    fun setWindowWidth(width: Int)

    /**
     * 设置容器高度
     */
    fun setWindowHeight(height: Int)
}