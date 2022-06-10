package com.xxf.rxlifecycle.demo

import android.icu.util.ULocale
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.Lifecycle
import autodispose2.AutoDispose
import com.xxf.rxjava.combineLatestDelayError
//import com.xxf.rxlifecycle.bindLifecycle
import io.reactivex.rxjava3.core.Observable
import java.text.Collator
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        Observable.interval(1, TimeUnit.SECONDS).bindLifecycle(this,untilEvent = Lifecycle.Event.ON_PAUSE).subscribe {
//            Log.d("=====>", "====yes" + it);
//        }


    }
}