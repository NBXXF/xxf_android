

@file:Suppress("unused")

package com.xxf.ktx

import android.app.Application
import android.content.Context
import androidx.startup.Initializer
import com.xxf.ktx.doOnActivityLifecycle

class AppInitializer : Initializer<Unit> {
  private var started = 0

  override fun create(context: Context) {
    application = context as Application
  }

  override fun dependencies() = emptyList<Class<Initializer<*>>>()

}
