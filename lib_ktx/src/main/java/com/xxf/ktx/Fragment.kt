

@file:Suppress("unused")

package com.xxf.ktx

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment


fun <T : Fragment> T.withArguments(vararg pairs: Pair<String, *>) = apply {
  arguments = bundleOf(*pairs)
}

fun <T> Fragment.arguments(key: String) = lazy<T?> {
  arguments[key]
}

fun <T> Fragment.arguments(key: String, default: T) = lazy {
  arguments[key] ?: default
}

fun <T> Fragment.safeArguments(name: String) = lazy<T> {
  checkNotNull(arguments[name]) { "No intent value for key \"$name\"" }
}

fun Fragment.doOnBackPressed(onBackPressed: () -> Unit) =
  requireActivity().doOnBackPressed(viewLifecycleOwner, onBackPressed)
