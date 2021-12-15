package com.xxf.adapter.demo

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.xxf.application.activity.bindExtra
import com.xxf.application.activity.putExtra

class TestFragment : Fragment {
    constructor() : super(R.layout.adapter_test) {
      //  putExtra("xx23");
    }

    private val param by bindExtra(default = "默认值啊")
    private val param2:String? by bindExtra()

    private var param3:String by bindExtra(default = "xx")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("=======>ex", "" + param);
        param3="xxx"
        Log.d("=======>ex2", "" + param3);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        param3="xxx4"
        Log.d("=======>ex3", "" + param3);
    }

    override fun onStop() {
        super.onStop()
        param3="xxx5"
        Log.d("=======>ex5", "" + param3);
    }
    override fun onDestroy() {
        super.onDestroy()

    }
}