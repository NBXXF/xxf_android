package com.xxf.fileprovider

import android.content.Context
import android.net.Uri
import java.io.File

/**
 * 转换授权URI
 */
fun File.toAuthorizedUri(context:Context): Uri =FileProvider7.getUriForFile(context,this)