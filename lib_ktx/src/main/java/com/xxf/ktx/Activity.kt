

@file:Suppress("unused")

package com.xxf.ktx

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import java.util.*


inline fun <reified T : Activity> Context.startActivity(
  vararg pairs: Pair<String, Any?>,
  crossinline block: Intent.() -> Unit = {}
) =
  startActivity(intentOf<T>(*pairs).apply(block))

fun Activity.finishWithResult(vararg pairs: Pair<String, *>) {
  setResult(Activity.RESULT_OK, Intent().putExtras(bundleOf(*pairs)))
  finish()
}

fun ComponentActivity.pressBackToNotExitApp(owner: LifecycleOwner = this) =
  doOnBackPressed(owner) { moveTaskToBack(false) }

fun ComponentActivity.doOnBackPressed(owner: LifecycleOwner = this, onBackPressed: () -> Unit) =
  onBackPressedDispatcher.addCallback(owner, object : OnBackPressedCallback(true) {
    override fun handleOnBackPressed() = onBackPressed()
  })


var Activity.decorFitsSystemWindows: Boolean
  @Deprecated(NO_GETTER, level = DeprecationLevel.ERROR)
  get() = noGetter()
  set(value) = WindowCompat.setDecorFitsSystemWindows(window, value)


inline val Activity.contentView: View
  get() = (findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0)

inline val Context.context: Context get() = this

inline val Activity.activity: Activity get() = this

inline val FragmentActivity.fragmentActivity: FragmentActivity get() = this

inline val ComponentActivity.lifecycleOwner: LifecycleOwner get() = this