package com.xxf.ktx

import android.media.MediaDrm
import java.util.UUID

/**
 * 数字版权的唯一id
 */
val DEVICE_UNIQUE_ID: String? by lazy {
    UUID(-0x121074568629b532L, -0x5c37d8232ae2de13L)
        .deviceUniqueId()
}

fun UUID.toMediaDrm(): MediaDrm {
    return MediaDrm(this)
}

fun UUID.deviceUniqueId(): String? {
    return kotlin.runCatching {
        toMediaDrm().use {
            it.getPropertyByteArray(MediaDrm.PROPERTY_DEVICE_UNIQUE_ID).toHexString()
        }
    }.getOrNull()
}