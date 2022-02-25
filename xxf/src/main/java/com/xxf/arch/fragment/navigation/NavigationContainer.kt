package com.xxf.arch.fragment.navigation


/**
 * 导航控制器容器
 */
interface NavigationContainer {

    /**
     * 导航控制器
     */
    fun getNavigationController(): INavigationController
}