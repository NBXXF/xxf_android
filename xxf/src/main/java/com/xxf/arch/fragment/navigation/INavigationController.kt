package com.xxf.arch.fragment.navigation

import androidx.fragment.app.Fragment

/**
 * @version 2.3.1
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description
 * @date createTime：2022/2/22
 */
interface INavigationController {

    /**
     * 下一个
     * @param destination 目标
     * @param anim 是否执行动画,默认左右
     * @param tag
     * @param flag 用于业务标记栈 考虑为activity的 flag 作用
     */
    fun navigation(destination: Fragment, anim: Boolean = true, tag: String? = null, flag: Int = -1)

    /**
     * 上一个 只有一个的时候返回false
     *  @param flag 用于业务标记栈 考虑为activity的 flag 作用
     *  Int.min 整个栈退出 既dialogfragment 或者bootomdialogfragment等 直接关闭
     */
    fun navigationUp(flag: Int = -1): Boolean

}