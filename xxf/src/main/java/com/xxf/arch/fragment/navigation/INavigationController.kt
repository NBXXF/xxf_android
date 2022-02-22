package com.xxf.arch.fragment.navigation

import androidx.fragment.app.Fragment

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
     */
    fun navigationUp(flag: Int = -1): Boolean

}