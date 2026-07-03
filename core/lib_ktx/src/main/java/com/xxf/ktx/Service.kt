package com.xxf.ktx

import android.Manifest
import android.app.Notification
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.core.app.ServiceCompat
import androidx.core.content.ContextCompat

/**
 * 建议继承 [android.app.ForegroundService]
 * 启动service 兼容各个版本
 * 在 Android 8.0 之前，创建前台 Service 的方式通常是先创建一个后台 Service，然后将该 Service 推到前台。
 * Android 8.0 有一项复杂功能：系统不允许后台应用创建后台 Service。
 * 因此，Android 8.0 引入了一种全新的方法，即 startForegroundService()，以在前台启动新 Service。、
 * 在系统创建 Service 后，应用有五秒的时间来调用该 Service 的 startForeground() 方法以显示新 Service 的用户可见通知。
 * 如果应用在此时间限制内_未_调用 startForeground()，则系统将停止此 Service 并声明此应用为 ANR。
 *
 * 前台判断
 * 如果满足以下任意条件，应用将被视为处于前台：
 * 具有可见 Activity（不管该 Activity 已启动还是已暂停）。
 * 具有前台 Service。
 * 另一个前台应用已关联到该应用（不管是通过绑定到其中一个 Service，还是通过使用其中一个内容提供程序）。 例如，如果另一个应用绑定到该应用的 Service，那么该应用处于前台：
 * IME
 * 壁纸 Service
 * 通知侦听器
 * 语音或文本 Service
 *
 * https://developer.android.google.cn/develop/background-work/services/foreground-services?hl=zh-cn
 *     <service
 *         android:name=".MyMediaPlaybackService"
 *         android:foregroundServiceType="mediaPlayback"
 *         android:exported="false">
 *     </service>
 */
@RequiresPermission(allOf = [Manifest.permission.FOREGROUND_SERVICE])
fun <T : Context> T.startServiceCompat(service: Intent): Boolean {
    return tryOrLogFalse {
        ContextCompat.startForegroundService(this, service)
    }
}

/**
 * 是否应该启动前台服务
 */
fun <T : Context> T.shouldStartForegroundService(): Boolean {
    return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
}


/**
 * 兼容启动前台服务
 * 建议继承 [android.app.ForegroundService]
 *
 * Service. startForeground(int, Notification, int) ，第三个参数 foregroundServiceType 已添加到 Build. VERSION_CODES. Q中。
 * 在 SDK 版本 Build. VERSION_CODES. Q之前 ，该方法调用时应 Service. startForeground(int, Notification) 不带 foregroundServiceType 参数。
 * 从 SDK Version Build. VERSION_CODES. Q开始，允许的 foregroundServiceType 为：
 * [ServiceInfo.FOREGROUND_SERVICE_TYPE_MANIFEST]
 * [ServiceInfo.FOREGROUND_SERVICE_TYPE_NONE]
 * [ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC]
 * [ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK]
 * [ServiceInfo.FOREGROUND_SERVICE_TYPE_PHONE_CALL]
 * [ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION]
 * [ServiceInfo.FOREGROUND_SERVICE_TYPE_CONNECTED_DEVICE]
 * [ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PROJECTION]
 * [ServiceInfo.FOREGROUND_SERVICE_TYPE_CAMERA]
 * [ServiceInfo.FOREGROUND_SERVICE_TYPE_MICROPHONE]
 * 从 SDK 版本 android. os. Build. VERSION_CODES 开始 。UPSIDE_DOWN_CAKE，以 SDK 版本 android. os. Build. VERSION_CODES 为目标平台的应用 。不允许 使用 ServiceInfo.FOREGROUND_SERVICE_TYPE_NONEUPSIDE_DOWN_CAKE 。
 * 允许的 foregroundServiceType 为：
 * [ServiceInfo.FOREGROUND_SERVICE_TYPE_MANIFEST]
 * [ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC]
 * [ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK]
 * [ServiceInfo.FOREGROUND_SERVICE_TYPE_PHONE_CALL]
 * [ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION]
 * [ServiceInfo.FOREGROUND_SERVICE_TYPE_CONNECTED_DEVICE]
 * [ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PROJECTION]
 * [ServiceInfo.FOREGROUND_SERVICE_TYPE_CAMERA]
 * [ServiceInfo.FOREGROUND_SERVICE_TYPE_MICROPHONE]
 * [ServiceInfo.FOREGROUND_SERVICE_TYPE_HEALTH]
 * [ServiceInfo.FOREGROUND_SERVICE_TYPE_REMOTE_MESSAGING]
 * [ServiceInfo.FOREGROUND_SERVICE_TYPE_SYSTEM_EXEMPTED]
 * [ServiceInfo.FOREGROUND_SERVICE_TYPE_SHORT_SERVICE]
 * [ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE]
 * 另请参见：
 * Service.startForeground(int, Notification), Service.startForeground(int, Notification, int)
 */
fun <T : Service> T.startForegroundCompat(
    id: Int,
    notification: Notification,
    foregroundServiceType: Int
): Boolean {
    //官方认定 https://developer.android.com/develop/background-work/services/foreground-services?hl=zh-cn
    //需try  建议用 ServiceCompat
    //要么就放弃START_STICKY
    //or use ServiceCompat.startForeground(this, id, notification, foregroundServiceType)
    return tryOrLogFalse {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(
                id,
                notification,
                foregroundServiceType,
            )
        } else {
            startForeground(id, notification)
        }
    }
}

/**
 * 兼容关闭前台服务
 * 从前台状态中删除传递的服务，以便在需要更多内存时将其终止。
 * 参数：
 * service – 服务删除。 flags – 其他行为选项： [androidx.core.app.ServiceCompat.STOP_FOREGROUND_REMOVE]、 [androidx.core.app.ServiceCompat.STOP_FOREGROUND_DETACH]
 * 另请参见：
 * Service. startForeground(int, Notification)
 *
 * 从前台移除服务
 * 要从前台移除服务，请调用 stopForeground()。 此方法采用布尔值，指示是否移除状态栏 通知。请注意，服务会继续运行。
 * 如果您在服务在前台运行时将其停止，其通知 已移除。
 */
fun <T : Service> T.stopForegroundCompat(
    @ServiceCompat.StopForegroundFlags flags: Int
): Boolean {
    //官方认定 https://developer.android.com/develop/background-work/services/foreground-services?hl=zh-cn
    //需try  建议用 ServiceCompat
    //要么就放弃START_STICKY
    return tryOrLogFalse {
        ServiceCompat.stopForeground(this, flags)
    }
}
