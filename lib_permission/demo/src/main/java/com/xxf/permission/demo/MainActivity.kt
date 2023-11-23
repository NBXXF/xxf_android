package com.xxf.permission.demo

import android.Manifest
import android.annotation.SuppressLint
import android.icu.util.ULocale
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.xxf.permission.requestPermissionsObservable
import java.util.*

class MainActivity : AppCompatActivity() {
    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.requestPermissionsObservable(Manifest.permission.INSTALL_PACKAGES)
            .subscribe {
                Toast.makeText(this, "结果:$it",Toast.LENGTH_LONG).show();
            }
    }

}