package com.xxf.arch.test

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xxf.arch.dialog.XXFDialog
import com.xxf.arch.test.databinding.DialogTestBinding
import com.xxf.utils.dp

class TestDialog(context: Context) : XXFDialog<Unit>(context,R.style.Test) {
    val binding by lazy {
        DialogTestBinding.inflate(LayoutInflater.from(context))
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setWindowSize(ViewGroup.LayoutParams.MATCH_PARENT, 200.dp)
        printView(binding.root)

        window?.setBackgroundDrawable(ColorDrawable(Color.RED))
        window!!.decorView!!.background=ColorDrawable(Color.YELLOW)
       // window!!.decorView!!.findViewById<View>(android.R.id.content)!!.background=ColorDrawable(Color.BLUE)
    }

    fun printView(view: View) {
        println("============>view:${view}")
        if (view.parent != null&&view.parent is View) {
            printView(view.parent as View)
        }
    }
}