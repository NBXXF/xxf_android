package android.app

import android.annotation.SuppressLint
import android.content.pm.ServiceInfo
import android.graphics.drawable.Icon
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.core.graphics.drawable.toBitmap
import com.xxf.ktx.appIcon
import com.xxf.ktx.notificationManagerCompat
import com.xxf.ktx.shouldStartForegroundService
import com.xxf.ktx.standard.lazyUnsafe
import com.xxf.ktx.startForegroundCompat


/**
 * @author XXF
 * @version 1.0
 * @since 2024/9/4 11:49
 * Android 8.0（API 级别26）开始，对于非前台服务的限制，
 * Google 推出了几种例外情况，如 MediaBrowserServiceCompat 或者 JobScheduler，但这些需要根据具体应用场景来判断是否适用。
 *  <uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
 */
abstract class ForegroundService : Service() {
    companion object {
        private val TAG = ForegroundService::class.java.simpleName
    }

    private val mNotificationId: Int by lazyUnsafe {
        (this.packageName + ForegroundService::class.java.name).hashCode()
    }
    private val mChannelId: String by lazyUnsafe {
        this.packageName + this::class.java.name
    }
    private val mChannelName: String by lazyUnsafe {
        this::class.java.simpleName
    }

    override fun onCreate() {
        super.onCreate()
        if (this.shouldStartForegroundService()) {
            this.onHandleForegroundServiceNotification()
        }
    }

    /**
     * 处理前台服务通知
     */
    protected open fun onHandleForegroundServiceNotification() {
        val started = this.startForegroundCompat(
            mNotificationId,
            onCreateNotification(),
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                ServiceInfo.FOREGROUND_SERVICE_TYPE_MANIFEST
            } else {
                0
            }
        )
        Log.i(TAG, "startForegroundCompat started:$started")
    }

    /**
     * 创建通知
     */
    @SuppressLint("RestrictedApi")
    protected open fun onCreateNotification(): Notification {
        createNotificationChannel()
        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, this.mChannelId)
                .setContentTitle("Foreground Service")
                .setContentText("${this::class.java.simpleName} is running in foreground")
                .apply {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        IconCompat.createFromIcon(
                            Icon.createWithBitmap(
                                this@ForegroundService.appIcon.toBitmap(
                                    128,
                                    128
                                )
                            )
                        )
                            ?.let { setSmallIcon(it) }
                    }
                }
                .setPriority(NotificationCompat.PRIORITY_HIGH)
        return notificationBuilder.build()
    }


    /**
     * IMPORTANCE_DEFAULT：默认的权限级别，会显示通知的所有内容。
     * IMPORTANCE_HIGH：高权限级别，会以高优先级显示通知，可能会打断用户。
     * IMPORTANCE_LOW：低权限级别，不会以高优先级显示通知，只会显示简单的通知内容。
     * IMPORTANCE_MIN：最低权限级别，通知将会被完全隐藏。
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                this.mChannelId,
                this.mChannelName,
                NotificationManager.IMPORTANCE_MIN
            )
            this.notificationManagerCompat.createNotificationChannel(notificationChannel)
        }
    }
}