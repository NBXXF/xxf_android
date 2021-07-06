package com.xxf.activityresult

import android.app.Activity
import android.content.Intent

class ActivityResult(val requestCode: Int, val resultCode: Int, val data: Intent) {
    val isOk: Boolean
        get() = resultCode == Activity.RESULT_OK
    val isCanceled: Boolean
        get() = resultCode == Activity.RESULT_CANCELED
}