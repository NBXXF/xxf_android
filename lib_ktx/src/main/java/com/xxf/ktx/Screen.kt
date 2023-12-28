

@file:Suppress("unused")

package com.xxf.ktx

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment

inline val Context.screenWidth: Int get() = resources.displayMetrics.widthPixels

inline val Context.screenHeight: Int get() = resources.displayMetrics.heightPixels

inline var Fragment.isFullScreen: Boolean
  get() = activity?.isFullScreen == true
  set(value) {
    activity?.isFullScreen = value
  }

inline var Activity.isFullScreen: Boolean
  get() = window.decorView.rootWindowInsetsCompat?.isVisible(WindowInsetsCompat.Type.systemBars()) == true
  set(value) {
    window.decorView.windowInsetsControllerCompat?.run {
      val systemBars = WindowInsetsCompat.Type.systemBars()
      if (value) show(systemBars) else hide(systemBars)
    }
  }

inline var Fragment.isLandscape: Boolean
  get() = activity?.isLandscape == true
  set(value) {
    activity?.isLandscape = value
  }

inline var Activity.isLandscape: Boolean
  get() = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
  set(value) {
    requestedOrientation = if (value) {
      ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    } else {
      ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }
  }

inline var Fragment.isPortrait: Boolean
  get() = activity?.isPortrait == true
  set(value) {
    activity?.isPortrait = value
  }

inline var Activity.isPortrait: Boolean
  get() = resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
  set(value) {
    requestedOrientation = if (value) {
      ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    } else {
      ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }
  }
