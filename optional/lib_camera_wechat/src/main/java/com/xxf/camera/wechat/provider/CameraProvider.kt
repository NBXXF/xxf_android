package com.xxf.camera.wechat.provider

import android.content.Context
import androidx.core.content.FileProvider


class CameraProvider : FileProvider() {
    companion object{
        fun getFileProviderName(context: Context): String {
            return context.packageName + ".provider"
        }
    }
}