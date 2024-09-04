package com.xxf.arch.test.service

import android.app.ForegroundService
import android.content.Intent
import android.os.IBinder

/**
 * @author XXF
 * @version 1.0
 * @since 2024/9/4 14:15
 * https://developer.android.google.cn/develop/background-work/services/foreground-services?hl=zh-cn
 *     <service
 *         android:name=".MyMediaPlaybackService"
 *         android:foregroundServiceType="mediaPlayback"
 *         android:exported="false">
 *     </service>
 */
class MyService : ForegroundService() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}