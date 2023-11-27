package com.xxf.permission.demo

import android.Manifest
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.xxf.permission.requestPermission
import java.util.*

class MainActivity : AppCompatActivity() {
    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.findViewById<View>(R.id.test).setOnClickListener {
            this.requestPermission(Manifest.permission.CAMERA)
                .subscribe {
                    Toast.makeText(this, "结果:$it",Toast.LENGTH_LONG).show();
                }
        }
    }

}