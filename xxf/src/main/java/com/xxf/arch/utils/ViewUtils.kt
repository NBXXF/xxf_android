package com.xxf.arch.utils

import android.util.TypedValue
import com.xxf.arch.XXF

val Float.dp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        XXF.getApplication().resources.displayMetrics
    )

val Float.sp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this,
        XXF.getApplication().resources.displayMetrics
    )

val Int.dp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        XXF.getApplication().resources.displayMetrics
    ).toInt()

val Int.sp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this.toFloat(),
        XXF.getApplication().resources.displayMetrics
    ).toInt()