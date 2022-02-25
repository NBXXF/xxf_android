package com.google.android.material.bottomsheet

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat

/**
 * @version 2.3.1
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description
 * @date createTime：2022/2/22
 */
class NavigationBottomSheetBehavior<V : View> : BottomSheetBehavior<V> {
    constructor() : super()
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)


    /**
     * 这里倒着遍历 寻找最后一个nest child
     */
    override fun findScrollingChild(view: View?): View? {
        if (ViewCompat.isNestedScrollingEnabled(view!!)) {
            return view
        }
        if (view is ViewGroup) {
            val group = view
            var i = group.childCount - 1
            while (i >= 0) {
                val scrollingChild = findScrollingChild(group.getChildAt(i))
                if (scrollingChild != null) {
                    return scrollingChild
                }
                i--
            }
        }
        return null
    }
}