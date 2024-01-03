package com.xxf.camera.wechat.config

import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.os.Environment
import android.util.Log
import java.io.File


class CameraConfig {
    companion object {
        var init = false
        var FRONT_CAMERA_ID: String = ""//正面摄像头ID
        lateinit var FRONT_CAMERA_CHARACTERISTIC: CameraCharacteristics//正面摄像头特征
        var BACK_CAMERA_ID: String = ""//背面摄像头ID
        lateinit var BACK_CAMERA_CHARACTERISTIC: CameraCharacteristics//背面摄像头特征

        //最后一次打开的摄像头
        var last_camera_id = ""
            get() {
                if (field.isEmpty()) {
                    field = BACK_CAMERA_ID
                }
                return field
            }

        //最后一次打开的摄像头的特征
        fun getCurrentCameraCameraCharacteristics(): CameraCharacteristics {
            return if (last_camera_id == BACK_CAMERA_ID) BACK_CAMERA_CHARACTERISTIC else FRONT_CAMERA_CHARACTERISTIC
        }

        //相机使用权限
        const val CAMERA_PERMISSION_CODE = 0x03701

        //最大录制时间
        var MAX_RECORD_TIME: Int = 15//秒数 默认15S

        //是否允许录像
        var IS_ALLOW_RECORD: Boolean = true

        //是否允许录拍照
        var IS_ALLOW_PHOTO: Boolean = true

        //视频质量 主要是配置帧率 范围定在 1 - 100
        var RECORD_QUALITY: Int = 30

        var test = 0

        /**
         * store
         */
        fun getSaveDir(context: Context):String{
            val dir = File("${context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)}")
            if (!dir.exists()){
                if (!dir.mkdirs()) {
                    Log.d("getSaveDir", "Failed to create directory.")
                    return ""
                }
            }
            return dir.absolutePath
        }
    }
}