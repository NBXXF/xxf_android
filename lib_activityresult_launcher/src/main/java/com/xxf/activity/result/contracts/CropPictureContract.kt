package com.xxf.activity.result.contracts

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContract
import androidx.annotation.CallSuper

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.1
 * @Description  跳转系统裁切界面  其他更多参考 [androidx.activity.result.contract.ActivityResultContracts]里面的静态类
 * @date createTime：2020/9/5
 */
data class CropPictureRequest @JvmOverloads constructor(
    val inputUri: Uri,
    var aspectX: Int = 1,
    var aspectY: Int = 1,
    var outputX: Int = 512,
    var outputY: Int = 512,
    var outputContentValues: ContentValues = ContentValues(),
)

class CropPictureContract : ActivityResultContract<CropPictureRequest, Uri?>() {
    private lateinit var outputUri: Uri

    @CallSuper
    override fun createIntent(context: Context, input: CropPictureRequest) =
        Intent("com.android.camera.action.CROP").apply {
            outputUri = context.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                input.outputContentValues
            )!!
            setDataAndType(input.inputUri, "image/*")
            putExtra(MediaStore.EXTRA_OUTPUT, outputUri)
            putExtra("aspectX", input.aspectX)
            putExtra("aspectY", input.aspectY)
            putExtra("outputX", input.outputX)
            putExtra("outputY", input.outputY)
            putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
            putExtra("return-data", false)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
        }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? =
        if (resultCode == Activity.RESULT_OK) outputUri else null
}