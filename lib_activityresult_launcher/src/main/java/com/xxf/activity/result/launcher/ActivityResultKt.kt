package com.xxf.activity.result.launcher

import android.app.Activity
import androidx.activity.result.ActivityResult
/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.1
 * @Description  ActivityResult 增加拓展
 * @date createTime：2018/9/5
 */
val ActivityResult.isOk:Boolean
    get() = resultCode == Activity.RESULT_OK
val ActivityResult.isCanceled: Boolean
    get() = resultCode == Activity.RESULT_CANCELED