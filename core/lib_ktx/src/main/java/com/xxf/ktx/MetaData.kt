

@file:Suppress("unused")

package com.xxf.ktx

import android.app.Activity
import android.app.Service
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager.GET_META_DATA

fun Context.applicationMetaDataOf(name: String): String? =
  this.packageManager.getApplicationInfo(this.packageName, GET_META_DATA).metaData.getString(name)

inline fun <reified T : Activity> Context.activityMetaDataOf(name: String): String? =
  this.packageManager.getActivityInfo(ComponentName<T>(), GET_META_DATA).metaData.getString(name)

inline fun <reified T : Service> Context.serviceMetaDataOf(name: String): String? =
  this.packageManager.getServiceInfo(ComponentName<T>(), GET_META_DATA).metaData.getString(name)

inline fun <reified T : BroadcastReceiver> Context.providerMetaDataOf(name: String): String? =
  this.packageManager.getProviderInfo(ComponentName<T>(), GET_META_DATA).metaData.getString(name)

inline fun <reified T : BroadcastReceiver> Context.receiverMetaDataOf(name: String): String? =
  this.packageManager.getReceiverInfo(ComponentName<T>(), GET_META_DATA).metaData.getString(name)

inline fun <reified T> Context.ComponentName() = ComponentName(this, T::class.java)
