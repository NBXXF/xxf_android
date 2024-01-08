package com.xxf.permission.transformer

import android.Manifest
import android.content.Context

/**
 * @version 2.3.1
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description 相机权限
 * @date createTime：2018/9/3
 */
@Deprecated("过时了,请用map PermissionCheckMapFunction 或者 PermissionCheckForResultMapFunction")
class CameraPermissionTransformer(context: Context) :
    RxPermissionTransformer(context, Manifest.permission.CAMERA)