

@file:Suppress("unused", "MemberVisibilityCanBePrivate", "CanBeParameter", "PropertyName")

package com.xxf.ktx

import android.Manifest.permission.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.*
import android.net.NetworkRequest
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.core.content.getSystemService
import androidx.lifecycle.LiveData
import com.xxf.ktx.application

@get:RequiresPermission(ACCESS_NETWORK_STATE)
val Context.isNetworkAvailable: Boolean
  get() = getSystemService<ConnectivityManager>()?.run {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      getNetworkCapabilities(activeNetwork)?.run {
        hasCapability(NET_CAPABILITY_INTERNET) && hasCapability(NET_CAPABILITY_VALIDATED)
      }
    } else {
      @Suppress("DEPRECATION")
      activeNetworkInfo?.isConnectedOrConnecting
    }
  } ?: false

@get:RequiresPermission(ACCESS_NETWORK_STATE)
val Context.isWifiConnected: Boolean
  get() = getSystemService<ConnectivityManager>()?.run {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      getNetworkCapabilities(activeNetwork)?.hasTransport(TRANSPORT_WIFI)
    } else {
      @Suppress("DEPRECATION")
      activeNetworkInfo?.run { isConnected && type == ConnectivityManager.TYPE_WIFI }
    }
  } ?: false

@get:RequiresPermission(ACCESS_NETWORK_STATE)
val Context.isMobileData: Boolean
  get() = getSystemService<ConnectivityManager>()?.run {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      getNetworkCapabilities(activeNetwork)?.hasTransport(TRANSPORT_CELLULAR)
    } else {
      @Suppress("DEPRECATION")
      activeNetworkInfo?.run { isAvailable && type == ConnectivityManager.TYPE_MOBILE }
    }
  } ?: false

@get:RequiresPermission(ACCESS_WIFI_STATE)
inline val Context.isWifiEnabled: Boolean
  get() = getSystemService<WifiManager>()?.isWifiEnabled == true

inline val ScanResult.is24GHz: Boolean
  get() = frequency in 2400..2550

inline val ScanResult.is5GHz: Boolean
  get() = frequency in 5500..5800

class NetworkAvailableLiveData @RequiresPermission(ACCESS_NETWORK_STATE) constructor(val context:Context) : LiveData<Boolean>() {

  private val connectivityManager = context.getSystemService<ConnectivityManager>()

  @RequiresPermission(ACCESS_NETWORK_STATE)
  override fun onActive() {
    super.onActive()
    when {
      Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ->
        connectivityManager?.registerDefaultNetworkCallback(networkCallback)
      else ->
        connectivityManager?.registerNetworkCallback(networkRequest, networkCallback)
    }
  }

  override fun onInactive() {
    super.onInactive()
    connectivityManager?.unregisterNetworkCallback(networkCallback)
  }

  override fun setValue(value: Boolean?) {
    if (this.value != value) {
      super.setValue(value)
    }
  }

  private val networkRequest by lazy {
    NetworkRequest.Builder()
      .addTransportType(TRANSPORT_CELLULAR)
      .addTransportType(TRANSPORT_ETHERNET)
      .addTransportType(TRANSPORT_WIFI)
      .build()
  }

  private val networkCallback = object : ConnectivityManager.NetworkCallback() {
    override fun onAvailable(network: Network) {
      if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
        postValue(true)
      }
    }

    override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        networkCapabilities.run {
          postValue(hasCapability(NET_CAPABILITY_INTERNET) && hasCapability(NET_CAPABILITY_VALIDATED))
        }
      }
    }

    override fun onLost(network: Network) {
      postValue(false)
    }
  }
}

class WifiListLiveData @RequiresPermission(allOf = [ACCESS_WIFI_STATE, CHANGE_WIFI_STATE]) constructor(val context: Context) : LiveData<List<WifiScanResult>?>() {

  private val wifiManager: WifiManager by lazy(LazyThreadSafetyMode.NONE) {
    context.getSystemService(Context.WIFI_SERVICE) as WifiManager
  }

  @Suppress("DEPRECATION")
  @Deprecated("The ability for apps to trigger scan requests will be removed in a future release.")
  fun startScan() {
    if (!wifiManager.startScan()) {
      value = null
    }
  }

  override fun onActive() {
    context.registerReceiver(wifiScanReceiver, IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
  }

  override fun onInactive() {
    context.unregisterReceiver(wifiScanReceiver)
  }

  private val wifiScanReceiver = object : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
      if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
        intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)
      ) {
        val results = mutableMapOf<String, WifiScanResult>()
        wifiManager.scanResults.filter { it.SSID.isNotEmpty() }.forEach { result ->
          if (results[result.SSID] == null) {
            results[result.SSID] = WifiScanResult(result)
          } else {
            results[result.SSID]?.addFrequency(result.frequency)
          }
        }
        value = results.values.toList()
      }
    }
  }
}

class WifiScanResult(val data: ScanResult) {
  @JvmField
  val SSID: String = data.SSID

  @JvmField
  val BSSID: String = data.BSSID

  @JvmField
  val capabilities: String = data.capabilities

  @JvmField
  val frequency: Int = data.frequency

  @JvmField
  val level: Int = data.level

  @JvmField
  val timestamp: Long = data.timestamp

  var is24GHz: Boolean = false
    private set

  var is5GHz: Boolean = false
    private set

  val isDualBand: Boolean get() = is24GHz && is5GHz

  init {
    addFrequency(data.frequency)
  }

  fun addFrequency(frequency: Int) {
    when (frequency) {
      in 2400..2550 -> is24GHz = true
      in 5500..5800 -> is5GHz = true
    }
  }
}
