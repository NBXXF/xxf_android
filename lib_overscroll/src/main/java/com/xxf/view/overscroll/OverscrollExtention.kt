package com.xxf.view.overscroll

import android.view.View
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import me.everything.android.ui.overscroll.IOverScrollDecor
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper


/**
拓展  文档参考:https://github.com/EverythingMe/overscroll-decor
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

fun HorizontalScrollView.setUpOverScroll(): IOverScrollDecor? {
    return OverScrollDecoratorHelper.setUpOverScroll(this)
}

fun View.setUpOverScroll(orientation: Int): IOverScrollDecor {
    return OverScrollDecoratorHelper.setUpStaticOverScroll(this, orientation);
}

fun ViewPager.setUpOverScroll(): IOverScrollDecor {
    return OverScrollDecoratorHelper.setUpOverScroll(this)
}