

@file:Suppress("unused")

package com.xxf.ktx

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

inline fun <T> Flow<T>.launchAndCollectIn(
  owner: LifecycleOwner,
  minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
  crossinline action: suspend CoroutineScope.(T) -> Unit
) = owner.lifecycleScope.launch {
  owner.repeatOnLifecycle(minActiveState) {
    collect {
      action(it)
    }
  }
}
