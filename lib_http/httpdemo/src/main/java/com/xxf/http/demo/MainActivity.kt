package com.xxf.http.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.gson.JsonObject
import com.xxf.arch.json.JsonUtils
import com.xxf.arch.websocket.WebSocketClient
import okhttp3.internal.ws.WebSocketExtensions

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toJsonString2 = JsonUtils.toJsonString(TT())
        System.out.println("=======>Tttt:" + toJsonString2);


        val toJsonString =
            JsonUtils.toJsonString(
                TestDTO(
                    "hello",
                    TestDTO.Type.TYPE_A,
                    TestDTO.Type2.TYPE_B2,
                    TestDTO.Gender.FEMAIL
                )
            );
        System.out.println("=======>T1:" + toJsonString);
        val toBean = JsonUtils.toBean(toJsonString, TestDTO::class.java)
        System.out.println("=======>T2:" + toBean);

        val wsc =
            WebSocketClient("http://dev.allflow.cn/ws/webSocket/eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1dWlkIjoiOWJjNDhmY2UtYWI1Yi00OTg3LWJmZDAtNTc2ZjhlY2RjODc5Iiwibmlja25hbWUiOiJCYmIiLCJwaG9uZSI6IjE3NjExNjM5MDgwIiwiaWF0IjoxNjI5ODU5ODU4LCJleHAiOjE2MzI0NTE4NTh9.Q7LJxgJSc7mURO9A7fkhe-N1i9gI7RpbFxxUo7lMybo");
        wsc.subTextMessage().subscribe {
            System.out.println("=======>收到:" + it);
        }

        //发送消息
        val json = JsonObject();
        json.addProperty("requestId", "3bec9204-a133-4f62-b6e9-f5965395ceb6");
        json.addProperty("type", "/subscribe");
        val body = JsonObject();
        body.addProperty("channel", "/pages/d0e7d6e1-8224-4df4-bfc2-0295587de654");
        json.add("data", body);
        wsc.send(json.toString());


        wsc.send(json.toString());
    }
}