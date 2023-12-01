package com.xxf.json.demo

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.Expose
import com.spirytusz.booster.kapt.BoosterTypeAdapterFactory
//import com.spirytusz.booster.kapt.BoosterTypeAdapterFactory
import com.squareup.moshi.Moshi
import com.xxf.json.Json
import com.xxf.json.copy
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(){
companion object {
    private const val TAG = "MainActivity"
}

    val common = GsonBuilder().create()

    val gson=GsonBuilder()
        .registerTypeAdapterFactory(BoosterTypeAdapterFactory())
        .create()
    val moshi = Moshi.Builder()
        .build()
    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        this.findViewById<View>(R.id.test).setOnClickListener {
            val uses= listOf<User>(User("xxx"));
            val toJson = Json.toJson(uses);
            println("====================>ser json:$toJson")
            val fromJson = Json.fromJson<List<User>>(toJson)
            println("====================>des json:$fromJson")

            testMap()

            testCopy()


        }


        this.findViewById<View>(R.id.bost).setOnClickListener {
            val json = json()

            val common = GsonBuilder().create()
            val boost = GsonBuilder()
                .registerTypeAdapterFactory(BoosterTypeAdapterFactory())
                .create()

            val commonTimeCost = traceOnceJson(common, json)
            val boostTimeCost = traceOnceJson(boost, json)
            println("=========================>speed:"+""""
                common time cost: ${TimeUnit.NANOSECONDS.toMicros(commonTimeCost) / 1000.0}
                boost time cost:  ${TimeUnit.NANOSECONDS.toMicros(boostTimeCost) / 1000.0}
            """.trimIndent())
        }

    }

    override fun onResume() {
        super.onResume()
        testGsonOrMoshi()
    }
    private fun traceOnceJson(gson: Gson, json: String): Long {
        val start = SystemClock.elapsedRealtimeNanos()
        val bean = kotlin.runCatching {
            repeat(10){
                gson.fromJson<Foo>(json, Foo::class.java)
            }
        }.onFailure {
            Log.d(TAG, Log.getStackTraceString(it))
        }.getOrNull()
        val end = SystemClock.elapsedRealtimeNanos()
        Log.d(TAG, "$bean")
        return end - start
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun testGsonOrMoshi(){
        val json = json()
        val take1= traceOnceJson(json){
            repeat(100){t->
                common.fromJson(it,Foo::class.java)
            }
            common.fromJson(it,Foo::class.java)
        }

        val take3= traceOnceJson(json){
            repeat(100){t->
                moshi.adapter<Foo>(Foo::class.java).fromJson(it)!!
            }
            moshi.adapter<Foo>(Foo::class.java).fromJson(it)!!
        }


        val take2= traceOnceJson(json){
            repeat(100){t->
                gson.fromJson(it,Foo::class.java)
            }
           gson.fromJson(it,Foo::class.java)
        }
        println("===================>ttt1:${take1/1000}")
        println("===================>ttt2:${take2/1000}")
        println("===================>ttt3:${take3/1000}")
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun traceOnceJson(json: String, callback:(json:String)->Foo): Long {
        val start = SystemClock.elapsedRealtimeNanos()
        val bean = kotlin.runCatching {
            callback(json)
        }.onFailure {
        }.getOrNull()
        val end = SystemClock.elapsedRealtimeNanos()
        return end - start
    }

    private fun json() = assets.open("test.json").bufferedReader().use { it.readText() }
    data class User(val name:String,
                    @Expose(serialize = true, deserialize = false)
                    val age:Int=100)

    data class User2(val name:String,
                    val age:Int=100)



    private fun testMap(){
        val map= mutableMapOf<String,User>("xx" to User("jack"))
        val fromJson = Json.fromJson<Map<String, User>>(Json.toJson(map))
        println("====================>json2:$fromJson")
    }

    private fun testCopy(){
        val uses= listOf<User2>(User2("apple"));
        val copy = uses.copy<List<User2>>();
        println("====================>json2:$copy")
    }
}