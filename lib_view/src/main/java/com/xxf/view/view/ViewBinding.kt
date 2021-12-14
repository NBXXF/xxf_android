package com.xxf.view.view

import android.app.Dialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.viewbinding.ViewBinding

@Deprecated("有局限性,请使用by viewBinding", replaceWith = ReplaceWith("viewBinding"))
fun <VB : ViewBinding> ComponentActivity.binding(inflate: (LayoutInflater) -> VB) = lazy {
    inflate(layoutInflater).also {
        setContentView(it.root)
    }
}

@Deprecated("有局限性,请使用by viewBinding", replaceWith = ReplaceWith("viewBinding"))
fun <VB : ViewBinding> Dialog.binding(inflate: (LayoutInflater) -> VB) = lazy {
    inflate(layoutInflater).also { setContentView(it.root) }
}

@Deprecated("有局限性,请使用by viewBinding", replaceWith = ReplaceWith("viewBinding"))
fun <VB : ViewBinding> ViewGroup.binding(
    inflate: (LayoutInflater, ViewGroup?, Boolean) -> VB,
    attachToParent: Boolean = true
) = lazy {
    inflate(LayoutInflater.from(context), if (attachToParent) this else null, attachToParent)
}


