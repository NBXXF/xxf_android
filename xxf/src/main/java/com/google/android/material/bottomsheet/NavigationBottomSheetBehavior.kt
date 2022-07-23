package com.google.android.material.bottomsheet

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPagerUtils
import androidx.viewpager2.widget.ViewPager2
import java.lang.ref.WeakReference

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
        if (view == null) {
            return null
        }
        /**
         * 必须要显示的view
         */
        if (ViewCompat.isNestedScrollingEnabled(view!!) && view.isShown) {
            return view
        }
        if (view is ViewPager) {
            val currentViewPagerChild: View = ViewPagerUtils.getCurrentView(view)
                ?: return null
            val scrollingChild = findScrollingChild(currentViewPagerChild)
            if (scrollingChild != null) {
                return scrollingChild
            }
        } else if (view is ViewPager2) {
            val currentViewPagerChild: View = ViewPagerUtils.getCurrentView(view)
                ?: return null
            val scrollingChild = findScrollingChild(currentViewPagerChild)
            if (scrollingChild != null) {
                return scrollingChild
            }
        } else if (view is ViewGroup) {
            val group = view
            var i = 0
            val count = group.childCount
            while (i < count) {
                val scrollingChild = findScrollingChild(group.getChildAt(i))
                if (scrollingChild != null) {
                    return scrollingChild
                }
                i++
            }
        }
        return null
    }

    /**
     * 重置可以滚动的组件
     */
    fun invalidateScrollingChild(): View? {
        val get = viewRef?.get()
        if (get != null) {
            nestedScrollingChildRef = WeakReference(findScrollingChild(get))
        }
        return nestedScrollingChildRef?.get()
    }

    /**
     * 支持自己来设置setNestScrollingChild
     */
    fun setNestScrollingChild(child: View?) {
        if (child != null) {
            /**
             * 必须要显示的view
             */
            if (ViewCompat.isNestedScrollingEnabled(child)&&child.isShown) {
                nestedScrollingChildRef = WeakReference(child)
            } else {
                throw IllegalArgumentException("child not isNestedScrollingEnabled")
            }
        } else {
            nestedScrollingChildRef = WeakReference(null)
        }
    }

    /**
     * 支持自己来设置setNestScrollingChild
     */
    fun setNestScrollingChildFromParent(parent: View?) {
        val findScrollingChild = findScrollingChild(parent)
        setNestScrollingChild(findScrollingChild)
    }
}