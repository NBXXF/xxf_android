@file:Suppress("unused")

package com.xxf.ktx

import android.os.Bundle
import androidx.fragment.app.Fragment

fun Fragment.makeSureNoNullArguments() {
    if (this.arguments == null) {
        this.arguments = Bundle()
    }
}

fun Fragment.doOnBackPressed(onBackPressed: () -> Unit) =
    requireActivity().doOnBackPressed(viewLifecycleOwner, onBackPressed)
