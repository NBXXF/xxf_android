

@file:Suppress("unused")

package com.xxf.ktx

inline fun <T> List<T>.percentage(predicate: (T) -> Boolean) =
  filter(predicate).size.toFloat() / size
