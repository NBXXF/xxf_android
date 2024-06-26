package com.xxf.effect.overscroll

import android.view.View
import android.webkit.WebView
import android.widget.*
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.xxf.effect.overscroll.myadapter.NestedScrollViewOverScrollDecorAdapter
import com.xxf.effect.overscroll.myadapter.WebViewOverScrollDecorAdapter
import me.everything.android.ui.overscroll.IOverScrollDecor
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper
import me.everything.android.ui.overscroll.VerticalOverScrollBounceEffectDecorator
import java.lang.RuntimeException


/**
 * @Description: objectBox
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2021/11/30 17:34
 */

/**
 * ios 反弹效果
拓展  文档参考:https://github.com/EverythingMe/overscroll-decor

边缘拉伸效果:
https://github.com/EverythingMe/overscroll-decor/pull/47

参考[com.xxf.view.overscroll.decorator.HorizontalOverScrollScaleEffectDecorator]
[com.xxf.view.overscroll.decorator.VerticalOverScrollScaleEffectDecorator]
 */

/**
 * @param orientation
 * [androidx.recyclerview.widget.RecyclerView.HORIZONTAL]
 * [androidx.recyclerview.widget.RecyclerView.VERTICAL]
 */
fun RecyclerView.setUpOverScroll(orientation: Int): IOverScrollDecor {
    return OverScrollDecoratorHelper.setUpOverScroll(this, orientation)
}

fun ListView.setUpOverScroll(): IOverScrollDecor {
    return OverScrollDecoratorHelper.setUpOverScroll(this)
}

fun GridView.setUpOverScroll(): IOverScrollDecor {
    return OverScrollDecoratorHelper.setUpOverScroll(this)
}

fun ScrollView.setUpOverScroll(): IOverScrollDecor {
    return OverScrollDecoratorHelper.setUpOverScroll(this)
}

fun HorizontalScrollView.setUpOverScroll(): IOverScrollDecor {
    return OverScrollDecoratorHelper.setUpOverScroll(this)
}

/**
 * @param orientation 等价:android.widget.LinearLayout.OrientationMode
 * [android.widget.LinearLayout.HORIZONTAL]
 * [android.widget.LinearLayout.VERTICAL]
 */
fun View.setUpOverScroll(orientation: Int): IOverScrollDecor {
    return OverScrollDecoratorHelper.setUpStaticOverScroll(this, orientation);
}

fun ViewPager.setUpOverScroll(): IOverScrollDecor {
    return OverScrollDecoratorHelper.setUpOverScroll(this)
}


fun NestedScrollView.setUpOverScroll(): IOverScrollDecor {
    return VerticalOverScrollBounceEffectDecorator(NestedScrollViewOverScrollDecorAdapter(this))
}

fun WebView.setUpOverScroll(): IOverScrollDecor {
    return VerticalOverScrollBounceEffectDecorator(WebViewOverScrollDecorAdapter(this))
}

/**
 * @param orientation
 * [androidx.recyclerview.widget.RecyclerView.HORIZONTAL]
 * [androidx.recyclerview.widget.RecyclerView.VERTICAL]
 */
fun ViewPager2.setUpOverScroll(orientation: Int): IOverScrollDecor {
    val firstChild = this.getChildAt(0)
    if (firstChild is RecyclerView) {
        return firstChild.setUpOverScroll(orientation)
    } else {
        throw RuntimeException("ViewPager2 has not RecyclerView")
    }
}