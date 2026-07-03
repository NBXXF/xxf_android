

@file:Suppress("unused")

package com.xxf.ktx

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.*
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment

fun View.getString(@StringRes id: Int): String =
  context.getString(id)

fun Fragment.getDimension(@DimenRes id: Int): Float =
  requireContext().getDimension(id)

fun View.getDimension(@DimenRes id: Int): Float =
  context.getDimension(id)

fun Context.getDimension(@DimenRes id: Int): Float =
  resources.getDimension(id)

@ColorInt
fun Fragment.getCompatColor(@ColorRes id: Int): Int =
  requireContext().getCompatColor(id)

@ColorInt
fun View.getCompatColor(@ColorRes id: Int): Int =
  context.getCompatColor(id)

@ColorInt
fun Context.getCompatColor(@ColorRes id: Int): Int =
  ResourcesCompat.getColor(resources, id, null)

fun Fragment.getCompatDrawable(@DrawableRes id: Int): Drawable? =
  requireContext().getCompatDrawable(id)

fun View.getCompatDrawable(@DrawableRes id: Int): Drawable? =
  context.getCompatDrawable(id)

fun Context.getCompatDrawable(@DrawableRes id: Int): Drawable? =
  ResourcesCompat.getDrawable(resources, id, null)

fun Fragment.getCompatFont(@FontRes id: Int): Typeface? =
  requireContext().getCompatFont(id)

fun View.getCompatFont(@FontRes id: Int): Typeface? =
  context.getCompatFont(id)

fun Context.getCompatFont(@FontRes id: Int): Typeface? =
  ResourcesCompat.getFont(this, id)

@ColorInt
@Deprecated("Use `String.parseColor()` instead.", ReplaceWith("colorString.parseColor()"))
fun parseColor(colorString: String): Int =
  Color.parseColor(colorString)
