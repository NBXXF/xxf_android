

@file:Suppress("unused")

package com.xxf.ktx

import android.app.Activity
import android.app.Service
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.pm.PackageManager.GET_META_DATA

fun applicationMetaDataOf(name: String): String? =
  application.packageManager.getApplicationInfo(application.packageName, GET_META_DATA).metaData.getString(name)

inline fun <reified T : Activity> activityMetaDataOf(name: String): String? =
  application.packageManager.getActivityInfo(ComponentName<T>(), GET_META_DATA).metaData.getString(name)

inline fun <reified T : Service> serviceMetaDataOf(name: String): String? =
  application.packageManager.getServiceInfo(ComponentName<T>(), GET_META_DATA).metaData.getString(name)

inline fun <reified T : BroadcastReceiver> providerMetaDataOf(name: String): String? =
  application.packageManager.getProviderInfo(ComponentName<T>(), GET_META_DATA).metaData.getString(name)

inline fun <reified T : BroadcastReceiver> receiverMetaDataOf(name: String): String? =
  application.packageManager.getReceiverInfo(ComponentName<T>(), GET_META_DATA).metaData.getString(name)

inline fun <reified T> ComponentName() = ComponentName(application, T::class.java)
