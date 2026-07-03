

@file:Suppress("unused")

package com.xxf.ktx

import android.content.ClipData
import android.content.ClipboardManager
import android.content.ClipboardManager.OnPrimaryClipChangedListener
import android.content.Context
import android.content.Intent
import android.net.Uri

fun CharSequence.copyToClipboard(label: CharSequence? = null) =
  (app.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
    .setPrimaryClip(ClipData.newPlainText(label, this))

fun Uri.copyToClipboard(label: CharSequence? = null) =
  (app.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
    .setPrimaryClip(ClipData.newUri(contentResolver, label, this))

fun Intent.copyToClipboard(label: CharSequence? = null) =
  (app.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
    .setPrimaryClip(ClipData.newIntent(label, this))

fun Context.getTextFromClipboard(): CharSequence? =
  (this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
    .primaryClip?.takeIf { it.itemCount > 0 }?.getItemAt(0)?.coerceToText(this)

fun clearClipboard() =
  (app.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
    .setPrimaryClip(ClipData.newPlainText(null, ""))

fun doOnClipboardChanged(listener: OnPrimaryClipChangedListener): OnPrimaryClipChangedListener =
  (app.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
    .addPrimaryClipChangedListener(listener).let { listener }

fun OnPrimaryClipChangedListener.cancel() =
  (app.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
    .removePrimaryClipChangedListener(this)
