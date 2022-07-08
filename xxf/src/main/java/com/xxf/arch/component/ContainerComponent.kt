package com.xxf.arch.component

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2018/9/7
 * Description ://控制
 */
interface ContainerComponent {
    /**
     * 设置容器大小
     */
    fun setSize(width: Int, height: Int)

    /**
     * 设置容器宽度
     */
    fun setWidth(width: Int)

    /**
     * 设置容器高度
     */
    fun setHeight(height: Int)
}