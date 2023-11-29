package com.xxf.json.demo

import android.Manifest
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.xxf.activityresult.contracts.EnableNotificationContract
import com.xxf.activityresult.startActivityForResult
import com.xxf.json.Json
import java.util.*

class MainActivity : AppCompatActivity() {
    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


//        this.startActivityForResult(
//            ActivityResultContracts.RequestPermission(),
//            Manifest.permission.CAMERA
//        ).subscribe {
//            println("================================>1111")
//            Toast.makeText(this, "结果11:$it", Toast.LENGTH_SHORT).show();
//        }
//        this.startActivityForResult(
//            ActivityResultContracts.RequestPermission(),
//            Manifest.permission.CAMERA
//        ).subscribe {
//            println("================================>2222")
//            Toast.makeText(this, "结果22:$it", Toast.LENGTH_SHORT).show();
//        }
        this.findViewById<View>(R.id.test).setOnClickListener {
//            this.startActivityForResult(EnableNotificationContract(), Unit)
//                .doOnError {
//                    Toast.makeText(this, "错误结果:$it", Toast.LENGTH_SHORT).show();
//                }
//                .subscribe {
//                    Toast.makeText(this, "结果:$it", Toast.LENGTH_SHORT).show();
//                }

            val uses= listOf<User>(User("xxx"));
            val toJson = Json.toJson(uses);
            val fromJson = Json.fromJson<List<User>>(toJson)
            println("====================>json:$fromJson")
        }
    }

    data class User(val name:String)

    override fun onPause() {
        super.onPause()
        println("============================>onPause")
    }

}