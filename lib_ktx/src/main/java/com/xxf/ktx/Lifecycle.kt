

@file:Suppress("unused")

package com.xxf.ktx

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope

fun Application.doOnActivityLifecycle(
  onActivityCreated: ((Activity, Bundle?) -> Unit)? = null,
  onActivityStarted: ((Activity) -> Unit)? = null,
  onActivityResumed: ((Activity) -> Unit)? = null,
  onActivityPaused: ((Activity) -> Unit)? = null,
  onActivityStopped: ((Activity) -> Unit)? = null,
  onActivitySaveInstanceState: ((Activity, Bundle?) -> Unit)? = null,
  onActivityDestroyed: ((Activity) -> Unit)? = null,
): Application.ActivityLifecycleCallbacks =
  object : Application.ActivityLifecycleCallbacks {
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
      onActivityCreated?.invoke(activity, savedInstanceState)
    }

    override fun onActivityStarted(activity: Activity) {
      onActivityStarted?.invoke(activity)
    }

    override fun onActivityResumed(activity: Activity) {
      onActivityResumed?.invoke(activity)
    }

    override fun onActivityPaused(activity: Activity) {
      onActivityPaused?.invoke(activity)
    }

    override fun onActivityStopped(activity: Activity) {
      onActivityStopped?.invoke(activity)
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
      onActivitySaveInstanceState?.invoke(activity, outState)
    }

    override fun onActivityDestroyed(activity: Activity) {
      onActivityDestroyed?.invoke(activity)
    }
  }.also {
    registerActivityLifecycleCallbacks(it)
  }

fun Fragment.doOnViewLifecycle(
  onCreateView: (() -> Unit)? = null,
  onStart: (() -> Unit)? = null,
  onResume: (() -> Unit)? = null,
  onPause: (() -> Unit)? = null,
  onStop: (() -> Unit)? = null,
  onDestroyView: (() -> Unit)? = null,
) =
  viewLifecycleOwner.doOnLifecycle(onCreateView, onStart, onResume, onPause, onStop, onDestroyView)

fun LifecycleOwner.doOnLifecycle(
  onCreate: (() -> Unit)? = null,
  onStart: (() -> Unit)? = null,
  onResume: (() -> Unit)? = null,
  onPause: (() -> Unit)? = null,
  onStop: (() -> Unit)? = null,
  onDestroy: (() -> Unit)? = null,
) =
  lifecycle.addObserver(object : DefaultLifecycleObserver {
    override fun onCreate(owner: LifecycleOwner) {
      onCreate?.invoke()
    }

    override fun onStart(owner: LifecycleOwner) {
      onStart?.invoke()
    }

    override fun onResume(owner: LifecycleOwner) {
      onResume?.invoke()
    }

    override fun onPause(owner: LifecycleOwner) {
      onPause?.invoke()
    }

    override fun onStop(owner: LifecycleOwner) {
      onStop?.invoke()
    }

    override fun onDestroy(owner: LifecycleOwner) {
      onDestroy?.invoke()
    }
  })

val Fragment.viewLifecycleScope get() = viewLifecycleOwner.lifecycleScope
