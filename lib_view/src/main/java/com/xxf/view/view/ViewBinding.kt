package com.next.space.base

import android.app.Dialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.databinding.ViewDataBinding
import androidx.viewbinding.ViewBinding

fun <VB : ViewBinding> ComponentActivity.binding(inflate: (LayoutInflater) -> VB) = lazy {
    inflate(layoutInflater).also {
        setContentView(it.root)
        if (this is ViewDataBinding) lifecycleOwner = this@binding
    }
}

fun <VB : ViewBinding> Dialog.binding(inflate: (LayoutInflater) -> VB) = lazy {
    inflate(layoutInflater).also { setContentView(it.root) }
}

fun <VB : ViewBinding> ViewGroup.binding(
    inflate: (LayoutInflater, ViewGroup?, Boolean) -> VB,
    attachToParent: Boolean = true
) = lazy {
    inflate(LayoutInflater.from(context), if (attachToParent) this else null, attachToParent)
}


