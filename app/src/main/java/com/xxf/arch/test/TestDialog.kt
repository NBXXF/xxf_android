package com.xxf.arch.test

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.xxf.arch.dialog.XXFDialog
import com.xxf.arch.test.databinding.DialogTestBinding
import com.xxf.ktx.dp


class TestDialog(context: Context) : XXFDialog<Unit>(context,R.style.Test), LifecycleOwner {
    private val lifecycleRegistry = LifecycleRegistry(this)

    override val lifecycle: Lifecycle
        get() = lifecycleRegistry

    val binding by lazy {
        DialogTestBinding.inflate(LayoutInflater.from(context))
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        setContentView(binding.root)
        setWindowSize(ViewGroup.LayoutParams.MATCH_PARENT, 200.dp)
        printView(binding.root)

        window?.setBackgroundDrawable(ColorDrawable(Color.RED))
        window!!.decorView!!.background=ColorDrawable(Color.YELLOW)
       // window!!.decorView!!.findViewById<View>(android.R.id.content)!!.background=ColorDrawable(Color.BLUE)
    }

    override fun onStart() {
        super.onStart()
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
    }

    override fun onStop() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
        super.onStop()
    }

    override fun dismiss() {
        super.dismiss()
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    }

    fun printView(view: View) {
        println("============>view:${view}")
        if (view.parent != null&&view.parent is View) {
            printView(view.parent as View)
        }
    }
}
