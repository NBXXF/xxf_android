package com.xxf.mlkit.imageproxy

import android.annotation.SuppressLint
import android.graphics.Matrix
import android.graphics.Rect
import android.media.Image
import androidx.camera.core.ImageInfo
import androidx.camera.core.ImageProxy
import androidx.camera.core.ImmutableImageInfo
import androidx.camera.core.impl.TagBundle
import java.nio.ByteBuffer

/**
 * A custom ImageProxy implementation that wraps around a ByteBuffer.
 */
@SuppressLint("RestrictedApi")
public class ByteBufferImageProxy(
    private val byteBuffer: ByteBuffer,  // The ByteBuffer containing image data
    private val width: Int,
    private val height: Int,
    private val format: Int,
    private val rotationDegrees: Int // Rotation degrees (0, 90, 180, or 270)
) : ImageProxy {

    private val mImageInfo: androidx.camera.core.ImageInfo

    // We assume this is a single plane for simplicity. If you have multi-plane data (e.g., YUV), you'll need to handle each plane.
    private val planes: Array<ImageProxy.PlaneProxy> = arrayOf(PlaneProxy(byteBuffer))

    init {
        mImageInfo = ImmutableImageInfo.create(
            TagBundle.emptyBundle(),
            image.timestamp,
            rotationDegrees,
            Matrix()
        )
    }

    override fun close() {
        // Optionally, you can manage resources here (e.g., ByteBuffer, etc.)
    }

    override fun getCropRect(): Rect {
        return Rect(0, 0, width, height) // Returning the full image area as the crop rect.
    }

    override fun setCropRect(rect: Rect?) {
        // Implement this if you want to allow cropping. Otherwise, you can ignore this method.
    }

    override fun getFormat(): Int {
        return format // Return the image format (e.g., YUV_420_888)
    }

    override fun getHeight(): Int {
        return height
    }

    override fun getWidth(): Int {
        return width
    }

    override fun getPlanes(): Array<ImageProxy.PlaneProxy> {
        return planes
    }

    override fun getImageInfo(): ImageInfo {
        return mImageInfo;
    }


    @SuppressLint("UnsafeOptInUsageError")
    override fun getImage(): Image {
        throw UnsupportedOperationException("getImage() is not supported for ByteBufferImageProxy.")
    }

    // A custom PlaneProxy that wraps around the ByteBuffer.
    private class PlaneProxy(private val byteBuffer: ByteBuffer) : ImageProxy.PlaneProxy {

        override fun getBuffer(): ByteBuffer {
            return byteBuffer
        }

        override fun getPixelStride(): Int {
            return 1 // For simplicity, assuming 1 byte per pixel.
        }

        override fun getRowStride(): Int {
            return byteBuffer.capacity() / 2 // This is just an example. You need to calculate row stride based on the format.
        }
    }
}
