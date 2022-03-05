package com.xxf.http.demo.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.gson.JsonObject
import com.xxf.arch.apiService
import com.xxf.arch.json.JsonUtils
import com.xxf.arch.utils.copy
import com.xxf.arch.websocket.WebSocketClient
import com.xxf.http.demo.*
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import retrofit2.CacheType

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        RxJavaPlugins.setErrorHandler { }


        val testModel = ExposeTestModel().apply {
            this.name = "hello"
            this.des = "x:" + System.currentTimeMillis()
        }
        System.out.println("==========>expose serlize:" + JsonUtils.toJsonString(testModel))

        val copy_1 = testModel.copy()
        System.out.println("==========>expose copy:" + copy_1)

        val copy_2 = testModel.copy(excludeUnSerializableField = true, excludeUnDeserializableField = true)
        System.out.println("==========>expose copy2:" + copy_2)


        val apply = JsonObject().apply {
            this.addProperty("age", 10.5)
        }
        val toBean1 = JsonUtils.toBean(apply, TestFloatDTO::class.java);

        System.out.println("=======>TT:" + toBean1);
        val copy = toBean1.copy()
        System.out.println("=======>TT cp:" + copy);

        val copy2 = toBean1.copy(TestFloatDTO::class.java)
        System.out.println("=======>TT cp2:" + copy2);

        val list = arrayListOf<TestFloatDTO>(copy2)
        System.out.println("=======>TT cp3:" + list.copy());
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

    override fun onResume() {
        super.onResume()
        testhttp()


        Thread(Runnable {
            val test = TestModel()
            test.ext = mutableMapOf<String, Any>().apply {
                put("name", "张三")
                put("age", 20)
                put("other", InnerDto().apply {
                    name = "儿子"
                    des = "好好学习"
                })
            }
            val json = JsonUtils.toJsonString(test)
            val toBean = JsonUtils.toBean(json, TestModel::class.java)
            System.out.println("===========>toBean:" + toBean)
        }).start()
    }

    private fun testhttp() {
        LoginApiService::class.java.apiService()
            .getCityModel(CacheType.firstCache)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                Log.d("==========>retry no", "" + it)
            }
            .subscribe {
                Log.d("==========>retry ye", "" + it)
            }
    }
}