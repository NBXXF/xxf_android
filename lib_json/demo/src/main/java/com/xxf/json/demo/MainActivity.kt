package com.xxf.json.demo

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.gson.annotations.Expose
import com.xxf.json.Json
import java.util.*

class MainActivity : AppCompatActivity() {
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
        }
    }

    data class User(val name:String,
                    @Expose(serialize = true, deserialize = false)
                    val age:Int=100)


    private fun testMap(){
        val map= mutableMapOf<String,User>("xx" to User("jack"))
        val fromJson = Json.fromJson<Map<String, User>>(Json.toJson(map))
        println("====================>json2:$fromJson")
    }
}