package com.xxf.adapter.demo

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.xxf.application.activity.bindExtra
import com.xxf.application.activity.putExtra

class TestFragment : Fragment {
    constructor() : super(R.layout.adapter_test) {
        putExtra("xx23");
    }

    private val param by bindExtra(default = "默认值啊")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("=======>ex", "" + param);
    }
}