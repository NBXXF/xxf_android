

@file:Suppress("unused")

package com.xxf.ktx

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.core.os.bundleOf

inline fun <reified T> Context.intentOf(vararg pairs: Pair<String, *>): Intent =
  intentOf<T>(bundleOf(*pairs))

inline fun <reified T> Context.intentOf(bundle: Bundle): Intent =
  Intent(this, T::class.java).putExtras(bundle)


fun Intent.clearTask(): Intent = addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)

fun Intent.clearTop(): Intent = addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

fun Intent.newDocument(): Intent = addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)

fun Intent.excludeFromRecents(): Intent = addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)

fun Intent.multipleTask(): Intent = addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK)

fun Intent.newTask(): Intent = addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

fun Intent.noAnimation(): Intent = addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)

fun Intent.noHistory(): Intent = addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)

fun Intent.singleTop(): Intent = addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)

fun Intent.grantReadUriPermission(): Intent = apply {
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
  }
}
