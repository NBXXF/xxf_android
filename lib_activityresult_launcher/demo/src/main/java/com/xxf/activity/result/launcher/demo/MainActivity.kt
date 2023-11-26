package com.xxf.activity.result.launcher.demo

import android.Manifest
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.xxf.activity.result.contracts.EnableBluetoothContract
import com.xxf.activity.result.contracts.EnableInstallUnknownAppSources
import com.xxf.activity.result.contracts.EnableLocationContract
import com.xxf.activity.result.contracts.EnableManageUnknownAppSources
import com.xxf.activity.result.contracts.EnableNotificationContract
import com.xxf.activity.result.startActivityForResult
import java.util.*

class MainActivity : AppCompatActivity() {
    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        this.startActivityForResult(
            ActivityResultContracts.RequestPermission(),
            Manifest.permission.CAMERA
        ).subscribe {
            println("================================>1111")
            Toast.makeText(this, "结果11:$it", Toast.LENGTH_SHORT).show();
        }
        this.startActivityForResult(
            ActivityResultContracts.RequestPermission(),
            Manifest.permission.CAMERA
        ).subscribe {
            println("================================>2222")
            Toast.makeText(this, "结果22:$it", Toast.LENGTH_SHORT).show();
        }
        this.findViewById<View>(R.id.test).setOnClickListener {
            this.startActivityForResult(EnableInstallUnknownAppSources(), Unit)
                .doOnError {
                    Toast.makeText(this, "错误结果:$it", Toast.LENGTH_SHORT).show();
                }
                .subscribe {
                    Toast.makeText(this, "结果:$it", Toast.LENGTH_SHORT).show();
                }
        }
    }

    override fun onPause() {
        super.onPause()
        println("============================>onPause")
    }

}