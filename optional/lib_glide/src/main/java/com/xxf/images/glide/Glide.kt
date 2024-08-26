package com.xxf.images.glide

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide


/**
 *  glide 会蹦 检查一下
 */
fun glideCheck(view: View, block: () -> Unit) {
    try {
        //检查一下
        Glide.with(view)
        block()
    } catch (e: Throwable) {
        e.printStackTrace()
    }
}


fun glideCheck(context: Context, block: () -> Unit) {
    try {
        //检查一下
        Glide.with(context)
        block()
    } catch (e: Throwable) {
        e.printStackTrace()
    }
}

fun glideCheck(context: Activity, block: () -> Unit) {
    try {
        //检查一下
        Glide.with(context)
        block()
    } catch (e: Throwable) {
        e.printStackTrace()
    }
}

fun glideCheck(context: FragmentActivity, block: () -> Unit) {
    try {
        //检查一下
        Glide.with(context)
        block()
    } catch (e: Throwable) {
        e.printStackTrace()
    }
}

fun glideCheck(fragment: Fragment, block: () -> Unit) {
    try {
        //检查一下
        Glide.with(fragment)
        block()
    } catch (e: Throwable) {
        e.printStackTrace()
    }
}