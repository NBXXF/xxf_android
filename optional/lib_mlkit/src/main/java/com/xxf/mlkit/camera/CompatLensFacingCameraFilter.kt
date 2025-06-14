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
        if (filter.isEmpty()) {
            Log.w(
                CompatLensFacingCameraFilter::class.java.simpleName,
                "===========>lensFacingCameraFilter filter is empty,cameraInfos size:${cameraInfos.size}"
            )
            ///一定要返回可修改的,cameraInfos 可能内部是at java.util.Collections$UnmodifiableCollection.retainAll(Collections.java:1118)
            return cameraInfos.toMutableList()
        }
        return filter.toMutableList();
    }
}