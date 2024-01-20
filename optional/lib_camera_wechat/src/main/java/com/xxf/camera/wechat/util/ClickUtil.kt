package com.xxf.camera.wechat.util



class ClickUtil {
    companion object {
        private const val MIN_DELAY_TIME = 500L
        var lastClickTime:Long = 0
        fun isFastClick():Boolean{
            var flag = false
            val currentClickTime = System.currentTimeMillis()
            if ((currentClickTime - lastClickTime) >= MIN_DELAY_TIME) {
                flag = true
            }
            lastClickTime = currentClickTime
            return flag
        }
    }
}