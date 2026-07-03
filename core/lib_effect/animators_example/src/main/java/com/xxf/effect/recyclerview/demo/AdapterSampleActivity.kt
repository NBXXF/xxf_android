package com.xxf.effect.recyclerview.demo

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xxf.effect.recyclerview.animators.FadeInAnimator
import com.xxf.effect.recyclerview.demo.R.*

/**
 * Created by Daichi Furiya / Wasabeef on 2020/08/26.
 */
class AdapterSampleActivity : AppCompatActivity() {

  internal enum class Type {
    AlphaIn {
      override operator fun get(context: Context): com.xxf.effect.recyclerview.adapters.AnimationAdapter {
        return com.xxf.effect.recyclerview.adapters.AlphaInAnimationAdapter(
            MainAdapter(
                context,
                SampleData.LIST.toMutableList()
            )
        )
      }
    },
    ScaleIn {
      override operator fun get(context: Context): com.xxf.effect.recyclerview.adapters.AnimationAdapter {
        return com.xxf.effect.recyclerview.adapters.ScaleInAnimationAdapter(
            MainAdapter(
                context,
                SampleData.LIST.toMutableList()
            )
        )
      }
    },
    SlideInBottom {
      override operator fun get(context: Context): com.xxf.effect.recyclerview.adapters.AnimationAdapter {
        return com.xxf.effect.recyclerview.adapters.SlideInBottomAnimationAdapter(
            MainAdapter(
                context,
                SampleData.LIST.toMutableList()
            )
        )
      }
    },
    SlideInLeft {
      override operator fun get(context: Context): com.xxf.effect.recyclerview.adapters.AnimationAdapter {
        return com.xxf.effect.recyclerview.adapters.SlideInLeftAnimationAdapter(
            MainAdapter(
                context,
                SampleData.LIST.toMutableList()
            )
        )
      }
    },
    SlideInRight {
      override operator fun get(context: Context): com.xxf.effect.recyclerview.adapters.AnimationAdapter {
        return com.xxf.effect.recyclerview.adapters.SlideInRightAnimationAdapter(
            MainAdapter(
                context,
                SampleData.LIST.toMutableList()
            )
        )
      }
    };

    abstract operator fun get(context: Context): com.xxf.effect.recyclerview.adapters.AnimationAdapter
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(layout.activity_adapter_sample)

    setSupportActionBar(findViewById(id.tool_bar))
    supportActionBar?.setDisplayShowTitleEnabled(false)

    val recyclerView = findViewById<RecyclerView>(id.list)
    recyclerView.layoutManager = if (intent.getBooleanExtra(MainActivity.KEY_GRID, true)) {
      GridLayoutManager(this, 2)
    } else {
      LinearLayoutManager(this)
    }

    val spinner = findViewById<Spinner>(id.spinner)
    spinner.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1).apply {
      for (type in Type.values()) add(type.name)
    }
    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
      override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        recyclerView.adapter = Type.values()[position][view.context].apply {
          setFirstOnly(true)
          setDuration(500)
          setInterpolator(OvershootInterpolator(.5f))
        }
      }

      override fun onNothingSelected(parent: AdapterView<*>) {
        // no-op
      }
    }

    recyclerView.itemAnimator = FadeInAnimator()
    val adapter = MainAdapter(this, SampleData.LIST.toMutableList())
    recyclerView.adapter = com.xxf.effect.recyclerview.adapters.AlphaInAnimationAdapter(adapter).apply {
      setFirstOnly(true)
      setDuration(500)
      setInterpolator(OvershootInterpolator(.5f))
    }
  }
}
