

@file:Suppress("unused")

package com.xxf.ktx

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import androidx.core.content.getSystemService
import androidx.lifecycle.LiveData
import com.xxf.ktx.application

inline val isLocationEnabled: Boolean
  get() = try {
    application.getSystemService<LocationManager>()?.isProviderEnabled(LocationManager.GPS_PROVIDER) == true
  } catch (e: Exception) {
    false
  }

class LocationEnabledLiveDate : LiveData<Boolean>() {

  override fun onActive() {
    value = isLocationEnabled
    application.registerReceiver(locationReceiver, IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION))
  }

  override fun onInactive() {
    application.unregisterReceiver(locationReceiver)
  }

  override fun setValue(value: Boolean?) {
    if (this.value != value) {
      super.setValue(value)
    }
  }

  private val locationReceiver = object : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
      value = isLocationEnabled
    }
  }
}
