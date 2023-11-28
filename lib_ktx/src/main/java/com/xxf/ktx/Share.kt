

@file:Suppress("unused")

package com.xxf.ktx

import android.content.Context
import android.net.Uri
import androidx.core.app.ShareCompat

fun Context.shareText(content: String, title: String? = null) =
  share("text/plain") {
    setText(content)
    setChooserTitle(title)
  }

fun Context.shareImage(imageUri: Uri, title: String? = null) =
  shareTextAndImage(null, imageUri, title)

fun Context.shareImages(imageUris: List<Uri>, title: String? = null) =
  shareTextAndImages(null, imageUris, title)

fun Context.shareTextAndImage(content: String?, imageUri: Uri, title: String? = null) =
  share("image/*") {
    setText(content)
    setStream(imageUri)
    setChooserTitle(title)
  }

fun Context.shareTextAndImages(content: String?, imageUris: List<Uri>, title: String? = null) =
  share("image/*") {
    setText(content)
    imageUris.forEach { addStream(it) }
    setChooserTitle(title)
  }

fun Context.shareFile(uri: Uri, title: String? = null, mimeType: String = uri.mimeType.orEmpty()) =
  share(mimeType) {
    setStream(uri)
    setChooserTitle(title)
  }

fun Context.shareFiles(uris: List<Uri>, title: String? = null, mimeType: String? = null) =
  share(mimeType ?: uris.firstOrNull()?.mimeType) {
    uris.forEach { addStream(it) }
    setChooserTitle(title)
  }

inline fun Context.share(mimeType: String?, crossinline block: ShareCompat.IntentBuilder.() -> Unit) =
  ShareCompat.IntentBuilder(this).setType(mimeType).apply(block).startChooser()
