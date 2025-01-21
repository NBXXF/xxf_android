package com.xxf.mlkit.model

import android.graphics.Rect
import androidx.camera.core.ImageProxy
import java.io.Serializable

class ImageProxyInfo(val cropRect: Rect, val format: Int, val width: Int, val height: Int) :
    Serializable {
    constructor(proxy: ImageProxy) : this(proxy.cropRect, proxy.format, proxy.width, proxy.height)
}
