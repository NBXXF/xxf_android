package com.xxf.view.overscroll

import android.view.View
import android.widget.*
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.xxf.view.overscroll.myadapter.NestedScrollViewOverScrollDecorAdapter
import me.everything.android.ui.overscroll.IOverScrollDecor
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper
import me.everything.android.ui.overscroll.VerticalOverScrollBounceEffectDecorator
import me.everything.android.ui.overscroll.adapters.ScrollViewOverScrollDecorAdapter


/**
拓展  文档参考:https://github.com/EverythingMe/overscroll-decor
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