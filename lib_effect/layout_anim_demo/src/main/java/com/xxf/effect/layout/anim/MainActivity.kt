package com.xxf.effect.layout.anim

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.xxf.effect.layout.anim.fragment.GridDemoFragment
import com.xxf.effect.layout.anim.fragment.ListDemoFragment
import com.xxf.effect.layout.anim.demo.R
import com.xxf.effect.layout.anim.demo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.listSample.setOnClickListener(this)
        binding.headerSample.setOnClickListener(this)
        binding.gridSample.setOnClickListener(this)

        supportFragmentManager.addOnBackStackChangedListener {
            binding.buttonContainer.alpha = if (supportFragmentManager.backStackEntryCount > 0) 0f else 1f
        }
    }

    override fun onClick(view: View) {
        val fragment = when (view.id) {
            R.id.listSample -> ListDemoFragment.instance(withHeader = false)
            R.id.headerSample -> ListDemoFragment.instance(withHeader = true)
            else -> GridDemoFragment()
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack(null)
            .commit()
    }
}