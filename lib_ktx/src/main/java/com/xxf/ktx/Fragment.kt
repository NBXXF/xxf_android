@file:Suppress("unused")

package com.xxf.ktx

import android.os.Bundle
import androidx.fragment.app.Fragment

fun Fragment.makeSureNoNullArguments() {
    if (this.arguments == null) {
        this.arguments = Bundle()
    }
}


/**
 * key value
 */
fun Fragment.putExtra(key: String, value: Any): Bundle {
    makeSureNoNullArguments()
    return arguments!!.putExtras(key to value)
}

/**
 * putExtras(
 *          "Key1" to "Value",
 *          "Key2" to 123,
 *          "Key3" to false,
 *          "Key4" to arrayOf("4", "5", "6")
 *      )
 */
fun <T> Fragment.putExtras(vararg params: Pair<String, T>): Bundle {
    makeSureNoNullArguments()
    return arguments!!.putExtras(*params)
}


fun Fragment.doOnBackPressed(onBackPressed: () -> Unit) =
    requireActivity().doOnBackPressed(viewLifecycleOwner, onBackPressed)
