package com.xxf.ktx.time

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.annotation.RequiresApi
import com.xxf.ktx.application
import com.xxf.ktx.standard.lazyUnsafe
import java.time.ZoneId
import java.util.TimeZone
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty


@delegate:RequiresApi(Build.VERSION_CODES.O)
val ZoneIdUTC: ZoneId by lazyUnsafe {
    ZoneId.of("UTC")
}
@delegate:RequiresApi(Build.VERSION_CODES.O)
val ZoneIdUTC8: ZoneId by lazyUnsafe {
    ZoneId.of("UTC+8")
}

val systemZoneId: ZoneId by object : ReadOnlyProperty<Any?, ZoneId> {
    private lateinit var zoneId: ZoneId

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getValue(thisRef: Any?, property: KProperty<*>): ZoneId {
        if (!::zoneId.isInitialized) {
            zoneId = ZoneId.systemDefault()
            application.registerReceiver(object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent?) {
                    if (intent?.action == Intent.ACTION_TIMEZONE_CHANGED) {
                        TimeZone.setDefault(null)
                        zoneId = ZoneId.systemDefault()
                    }
                }
            }, IntentFilter(Intent.ACTION_TIMEZONE_CHANGED))
        }
        return zoneId
    }
}