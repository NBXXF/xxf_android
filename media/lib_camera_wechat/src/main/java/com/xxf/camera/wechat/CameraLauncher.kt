package com.xxf.camera.wechat

import android.Manifest
import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.fragment.app.FragmentActivity
import com.xxf.activityresult.isOk
import com.xxf.activityresult.startActivityForResult
import com.xxf.camera.wechat.config.CameraConfig
import com.xxf.permission.requestPermission
import com.xxf.permission.transformer.RxPermissionTransformer
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableSource
import io.reactivex.rxjava3.functions.Function


class CameraLauncher {

    /**
     * 打开前置摄像头， 默认打开上次退出时候的摄像头
     */
    fun openPreCamera(): CameraLauncher {
        CameraConfig.last_camera_id = CameraConfig.FRONT_CAMERA_ID
        return this
    }

    /**
     * 设置最大录制时间
     */
    fun setMaxRecordTime(sec: Int): CameraLauncher {
        if (sec < 1) {
            return this
        }
        CameraConfig.MAX_RECORD_TIME = sec
        return this
    }

    /**
     * 是否允许录像
     */
    fun allowRecord(boolean: Boolean): CameraLauncher {
        CameraConfig.IS_ALLOW_RECORD = boolean
        return this
    }

    /**
     * 是否允许拍照
     */
    fun allowPhoto(boolean: Boolean): CameraLauncher {
        CameraConfig.IS_ALLOW_PHOTO = boolean
        return this
    }

    /**
     * 设置视频质量 1 - 100
     */
    fun setRecordQuality(quality: Int): CameraLauncher {
        if (quality in 1..100) {
            CameraConfig.RECORD_QUALITY = quality
        } else {
            CameraConfig.RECORD_QUALITY = 30
        }
        return this
    }

    /**
     * 自动请求权限
     */
    fun forResult(activity: FragmentActivity, requestCode: Int): Observable<CameraResult> {
        return Observable
                .defer<CameraResult> {
                    activity.requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.CAMERA,
                                    Manifest.permission.RECORD_AUDIO)
                            .compose(RxPermissionTransformer(activity,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.CAMERA,
                                    Manifest.permission.RECORD_AUDIO))
                            .flatMap(Function<Boolean?, ObservableSource<ActivityResult>> {
                                activity.startActivityForResult(Intent(activity, CameraActivity::class.java))
                            }).subscribeOn(AndroidSchedulers.mainThread())
                            .flatMap {
                                if (it.isOk) {
                                    val resultIsImg = resultIsImg(it.data);
                                    Observable.just(CameraResult(getResultPath(it.data), resultIsImg));
                                } else {
                                    Observable.empty<CameraResult>();
                                }
                            }.subscribeOn(AndroidSchedulers.mainThread());
                }
    }

    companion object {
        const val CAPTURE_RESULT_IS_IMG = "CaptureResultIsImg"
        const val CAPTURE_RESULT = "CaptureResult"
        fun resultIsImg(data: Intent?): Boolean {
            return data?.getBooleanExtra(CAPTURE_RESULT_IS_IMG, false)?:false
        }

        fun getResultPath(data: Intent?): String {
            return data?.getStringExtra(CAPTURE_RESULT)?:""
        }

        val instance: CameraLauncher by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            CameraLauncher()
        }
    }
}