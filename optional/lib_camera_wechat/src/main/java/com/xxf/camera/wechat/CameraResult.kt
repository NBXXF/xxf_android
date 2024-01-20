package com.xxf.camera.wechat

import java.io.Serializable

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080
 * Date: 3/9/21 2:16 PM
 * Description: 结果
 */
data class CameraResult(val path: String, var isImage: Boolean) : Serializable;