package com.xxf.mlkit.model

import android.graphics.Bitmap
import android.graphics.Rect
import androidx.camera.core.ImageProxy
import com.xxf.utils.BitmapUtils
import java.io.Serializable

class ImageProxyInfo(
    val bitmap: Bitmap,
    val cropRect: Rect,
    val format: Int,
    val width: Int,
    val height: Int
) :
    Serializable {
    constructor(proxy: ImageProxy) : this(
        BitmapUtils.rotateBitmap(
            proxy.toBitmap(),
            proxy.imageInfo.rotationDegrees,
            flipX = false,
            flipY = false
        ),
        proxy.cropRect,
        proxy.format,
        proxy.width,
        proxy.height
    )
}
