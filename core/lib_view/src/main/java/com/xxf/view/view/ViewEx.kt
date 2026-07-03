package com.xxf.view.view

import android.view.View
import androidx.annotation.Px
import androidx.annotation.RequiresApi

/**
 *  改变才更新
 * Updates this view's relative padding. This version of the method allows using named parameters
 * to just set one or more axes.
 *
 * @see View.setPaddingRelative
 */
@RequiresApi(17)
public inline fun View.updatePaddingRelativeChange(
    @Px start: Int = paddingStart,
    @Px top: Int = paddingTop,
    @Px end: Int = paddingEnd,
    @Px bottom: Int = paddingBottom
) {
    if (start != paddingStart || top != paddingTop || end != paddingEnd || bottom != paddingBottom) {
        setPaddingRelative(start, top, end, bottom)
    }
}

/**
 *  * 改变才更新
 * Updates this view's padding. This version of the method allows using named parameters
 * to just set one or more axes.
 *
 * @see View.setPadding
 */
public inline fun View.updatePaddingChange(
    @Px left: Int = paddingLeft,
    @Px top: Int = paddingTop,
    @Px right: Int = paddingRight,
    @Px bottom: Int = paddingBottom
) {
    if (left != paddingLeft || top != paddingTop || right != paddingRight || bottom != paddingBottom) {
        setPadding(left, top, right, bottom)
    }
}

/**
 * 改变才更新
 * Sets the view's padding. This version of the method sets all axes to the provided size.
 *
 * @see View.setPadding
 */
public inline fun View.setPaddingChange(@Px size: Int) {
    if (size != paddingStart || size != paddingTop || size != paddingEnd || size != paddingBottom) {
        setPadding(size, size, size, size)
    }
}