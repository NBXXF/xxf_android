package com.xxf.bus.demo

import android.app.Activity
import android.net.nsd.NsdManager
import android.os.Bundle
import android.util.AndroidException
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.xxf.bus.postEvent
import com.xxf.bus.subscribeEvent

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun onResume() {
        super.onResume()
        TestEvent::class.java.subscribeEvent()
                .subscribe {
            System.out.println("=====>"+"收到"+it);
            runOnUiThread {
                        Toast.makeText(this, "收到" + it, Toast.LENGTH_SHORT).show();
                    }
        }
        String.javaClass.subscribeEvent()
            .subscribe {

            }
        TestEvent("测试").postEvent();
        "测试".postEvent();
    }
}