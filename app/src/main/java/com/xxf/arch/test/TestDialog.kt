package com.xxf.arch.test

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.xxf.arch.dialog.XXFDialog
import com.xxf.arch.test.databinding.DialogTestBinding
import com.xxf.utils.dp

class TestDialog(context: Context) : XXFDialog<Unit>(context) {
    val binding  by lazy {
        DialogTestBinding.inflate(LayoutInflater.from(context))
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setWindowSize(ViewGroup.LayoutParams.MATCH_PARENT,200.dp)
    }
}