package com.xxf.mlkit.camera

import android.annotation.SuppressLint
import android.util.Log
import androidx.camera.core.CameraInfo
import androidx.camera.core.impl.LensFacingCameraFilter

/**
 * 兼容摄像头,必须保证至少有一个摄像头
 */
class CompatLensFacingCameraFilter : LensFacingCameraFilter {
    @SuppressLint("RestrictedApi")
    constructor(lensFacing: Int) : super(lensFacing)

    @SuppressLint("RestrictedApi")
    override fun filter(cameraInfos: MutableList<CameraInfo>): MutableList<CameraInfo> {
        val filter = super.filter(cameraInfos)
        if (filter.isEmpty() && cameraInfos.isNotEmpty()) {
            Log.w(
                CompatLensFacingCameraFilter::class.java.simpleName,
                "===========>lensFacingCameraFilter is empty"
            )
            return mutableListOf(cameraInfos.first())
        }
        return filter;
    }
}