package com.xxf.activity.result.launcher.demo

import android.Manifest
import android.annotation.SuppressLint
import android.icu.util.ULocale
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.xxf.activity.result.launcher.startActivityForResult
import java.util.*

class MainActivity : AppCompatActivity() {
    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        this.startActivityForResult(ActivityResultContracts.RequestPermission(),Manifest.permission.CAMERA).subscribe {
            println("================================>1111")
            Toast.makeText(this, "结果11:$it",Toast.LENGTH_LONG).show();
        }
        this.startActivityForResult(ActivityResultContracts.RequestPermission(),Manifest.permission.CAMERA).subscribe {
            println("================================>2222")
            Toast.makeText(this, "结果22:$it",Toast.LENGTH_LONG).show();
        }
        this.findViewById<View>(R.id.test).setOnClickListener {
            this.startActivityForResult(ActivityResultContracts.RequestPermission(),Manifest.permission.CAMERA).subscribe {
                Toast.makeText(this, "结果:$it",Toast.LENGTH_LONG).show();
            }
        }
    }

    override fun onPause() {
        super.onPause()
        println("============================>onPause")
    }

}