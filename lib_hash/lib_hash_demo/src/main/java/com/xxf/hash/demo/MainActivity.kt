package com.xxf.hash.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.xxf.hash.city.cityhash.CityHash
import com.xxf.hash.toMurmurHash

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        System.out.println("=================>take start")
        Thread(Runnable {
            val id = "fdgghghdfgiggsggsfgdfafffgfdsjhfhgghsgfffafgagjdfsfgsdfgdfgdfdfdsfdsfgdsfgdfdsfgdfgdsfgdsfgfgdsfgdsfgdgfgcgvgv"
            var start = System.currentTimeMillis()
            val cityHash = CityHash()
            for (i in 0..100000) {
                val hash32 = cityHash.hash32( "${id} $i")
            }
            System.out.println("================>take:" + (System.currentTimeMillis() - start))

            start = System.currentTimeMillis()
            for (i in 0..100000) {
                val toMurmurHash = "${id} $i".toMurmurHash()
            }
            System.out.println("================>take2:" + (System.currentTimeMillis() - start))
        }).start()
    }
}