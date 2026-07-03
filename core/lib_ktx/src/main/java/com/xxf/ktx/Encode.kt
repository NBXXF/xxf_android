

@file:Suppress("unused")

package com.xxf.ktx

import android.util.Base64
import java.net.URLDecoder
import java.net.URLEncoder

fun ByteArray.base64Encode(flag: Int = Base64.DEFAULT): ByteArray =
  Base64.encode(this, flag)

fun ByteArray.base64EncodeToString(flag: Int = Base64.DEFAULT): String =
  Base64.encodeToString(this, flag)

fun String.base64Decode(flag: Int = Base64.DEFAULT): ByteArray =
  Base64.decode(this, flag)

fun String.urlEncode(enc: String): String =
  URLEncoder.encode(this, enc)

fun String.urlDecode(enc: String): String =
  URLDecoder.decode(this, enc)
