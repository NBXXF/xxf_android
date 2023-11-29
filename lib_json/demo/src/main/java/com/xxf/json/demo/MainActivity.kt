package com.xxf.json.demo

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.gson.annotations.Expose
import com.xxf.json.Json
import com.xxf.json.copy
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

            testCopy()
        }
    }

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