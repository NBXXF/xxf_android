

@file:Suppress("unused")

package com.xxf.ktx

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import androidx.core.content.getSystemService
import androidx.lifecycle.LiveData

inline val isLocationEnabled: Boolean
  get() = try {
    app.getSystemService<LocationManager>()?.isProviderEnabled(LocationManager.GPS_PROVIDER) == true
  } catch (e: Exception) {
    false
  }

class LocationEnabledLiveDate : LiveData<Boolean>() {

  override fun onActive() {
    value = isLocationEnabled
    app.registerReceiver(locationReceiver, IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION))
  }

  override fun onInactive() {
    app.unregisterReceiver(locationReceiver)
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
