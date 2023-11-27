package com.xxf.activityresult.contracts

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContract

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.1
 * @Description  按mime 筛选文件  其他更多参考 [androidx.activity.result.contract.ActivityResultContracts]里面的静态类
 * @date createTime：2020/9/5
 */
class PickImageFromGalleryContract : ActivityResultContract<Unit, Uri?>() {
    override fun createIntent(context: Context, input: Unit) =
        Intent(Intent.ACTION_PICK).apply {
            this.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        return if (intent == null || resultCode != Activity.RESULT_OK) null else intent.data!!
    }
}