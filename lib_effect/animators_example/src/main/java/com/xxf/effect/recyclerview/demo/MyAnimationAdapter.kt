package com.xxf.effect.recyclerview.demo

import androidx.recyclerview.widget.RecyclerView
import com.xxf.effect.recyclerview.adapters.AlphaInAnimationAdapter

class MyAnimatorAdapter constructor(
  adapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>,
  from: Float = 0.5f
) : com.xxf.effect.recyclerview.adapters.AlphaInAnimationAdapter(adapter, from) {
}
