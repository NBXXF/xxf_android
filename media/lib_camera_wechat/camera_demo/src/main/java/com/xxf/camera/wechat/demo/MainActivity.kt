package com.xxf.camera.wechat.demo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.xxf.camera.wechat.CameraLauncher
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val PHOTO_OR_VIDEO_FOR_CAMERA = 0x3701
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RxJavaPlugins.setErrorHandler {
            Log.d("=======>error:", "" + it);
        }
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        text.text = "请按下拍照按钮\nPlease pressed camera button"
        fab.setOnClickListener {
            //如不设置 会默认打开上一次配置的结果
            CameraLauncher.instance
                    //.openPreCamera()// 是否打开为前置摄像头
                    //.allowPhoto(true)// 是否允许拍照 默认允许
                    //.allowRecord(true)// 是否允许录像 默认允许
                    //.setMaxRecordTime(3)//最长录像时间 秒
                    .forResult(this, PHOTO_OR_VIDEO_FOR_CAMERA)
                    .subscribe {
                        if (it.isImage) {
                            text.text = "Image Path：\n${it.path}"
                        } else {
                            text.text = "Video Path：\n${it.path}"
                        }
                    }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

}
