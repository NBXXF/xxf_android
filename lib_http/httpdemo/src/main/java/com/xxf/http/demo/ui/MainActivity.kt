package com.xxf.http.demo.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Switch
import com.google.gson.*
import com.google.gson.annotations.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.xxf.arch.apiService
import com.xxf.arch.getApiService
import com.xxf.arch.http.converter.gson.GsonConverterFactory
import com.xxf.arch.websocket.WebSocketClient
import com.xxf.http.demo.*
import com.xxf.http.demo.ui.test.Animal
import com.xxf.http.demo.ui.test.Person
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.serialization.*
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.json.Json
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    class Demo {
        var name: String? = null

//        class Jsonadapter : TypeAdapter<String?>() {
//
//            override fun write(out: JsonWriter?, value: String?) {
//                System.out.println("=============>ser src:" + value)
//                out?.value(value)
//            }
//
//            override fun read(`in`: JsonReader?): String? {
//                return `in`?.nextString()
//            }
//        }
    }

    class Demo2 {
        var name: String? = "xxx"
        override fun toString(): String {
            return "Demo2(name=$name)"
        }

//        class Jsonadapter : TypeAdapter<String?>() {
//
//            override fun write(out: JsonWriter?, value: String?) {
//                System.out.println("=============>ser src:" + value)
//                out?.value(value)
//            }
//
//            override fun read(`in`: JsonReader?): String? {
//                return `in`?.nextString()
//            }
//        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val findViewById1 = findViewById<View>(R.id.root);
        findViewById1.setOnLongClickListener {
            println("=================>长按了")
            false;
        }

        RxJavaPlugins.setErrorHandler { }
        GsonConverterFactory.setOnGsonConvertFailListener { gson, adapter, json, e ->
            println("=================>gson解析异常了$e  json: ${json}")
        }


        val apply1 = JsonObject().apply {
            addProperty("name", "张三")
            addProperty("desc", "我是个人")
            addProperty("bite", "我不咬人")
        }



        val editText = findViewById<MyEditText>(R.id.edit_text)
        val findViewById = findViewById<Switch>(R.id.btn_test)
        findViewById.setOnCheckedChangeListener { buttonView, isChecked ->
            editText.isRead = isChecked
        }






        val wsc =
            WebSocketClient(this,"http://dev.allflow.cn/ws/webSocket/eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1dWlkIjoiOWJjNDhmY2UtYWI1Yi00OTg3LWJmZDAtNTc2ZjhlY2RjODc5Iiwibmlja25hbWUiOiJCYmIiLCJwaG9uZSI6IjE3NjExNjM5MDgwIiwiaWF0IjoxNjI5ODU5ODU4LCJleHAiOjE2MzI0NTE4NTh9.Q7LJxgJSc7mURO9A7fkhe-N1i9gI7RpbFxxUo7lMybo");
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
        //   test()
        // testJsonSpeed()

        testJson2()
//        Thread(Runnable {
//            val list = mutableListOf<StringClass>()
//            for (i in 0..100000) {
//                list.add(StringClass().apply {
//                    this.type = "" + Random.nextInt(20)
//                })
//            }
//            val toJsonString = JsonUtils.toJsonString(list)
//            var start = System.currentTimeMillis()
//            val toBeanList2 = JsonUtils.toBeanList(toJsonString, StringClass::class.java)
//            System.out.println("=============>take enum string:" + (System.currentTimeMillis() - start))
//
//            start = System.currentTimeMillis()
//            val toBeanList = JsonUtils.toBeanList(toJsonString, EnumClass::class.java)
//            System.out.println("=============>take enum:" + (System.currentTimeMillis() - start))
//
//            start = System.currentTimeMillis()
//            for (i in 0..100) {
//                JsonUtils.toBeanList(toJsonString, StringClass::class.java)
//            }
//            System.out.println("=============>take enum string(100000):" + (System.currentTimeMillis() - start))
//
//            start = System.currentTimeMillis()
//            for (i in 0..100) {
//                JsonUtils.toBeanList(toJsonString, EnumClass::class.java)
//            }
//            System.out.println("=============>take enum(100000):" + (System.currentTimeMillis() - start))
//
//        }).start()


    }

    @SuppressLint("CheckResult")
    private fun testhttp() {
        getApiService<LoginApiService>()
            .getCity()
            //.getCity(TestQueryJsonField("xxx"))
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                Log.d("==========>retry no", "" + it)
            }
            .subscribe {
                Log.d("===========>gson xxxx", "" + it)
            }
    }

    private fun testJsonSpeed() {
//        Observable.create<Long> {
//            Thread.sleep(5001)
//            it.onNext(1)
//            Thread.sleep(5000)
//
//            it.onNext(2)
//            Thread.sleep(300)
//            it.onNext(3)
//            Thread.sleep(300)
//
//            it.onNext(4)
//            Thread.sleep(5000)
//            it.onNext(5)
//            it.onComplete()
//        }
        Observable.interval(5, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .sample(5, TimeUnit.SECONDS, true)
            .doOnComplete {
                System.out.println("===================>next complete:")
            }
            .subscribe {
                System.out.println("===================>next:" + it)
            }
        Thread(Runnable {
            val moshi: Moshi = Moshi.Builder()
                .addLast(KotlinJsonAdapterFactory())
                .build()
            val gson = Gson()

            val list = mutableListOf<InnerDto2>()
            for (i in 0..100000) {
                list.add(InnerDto2().apply {
                    name = "" + i;
                    des = "" + i;
//                    this.mp = mutableMapOf<String, String>().apply {
//                        put("xx", "21")
//                        put("24", "xx")
//                    }
                })
            }
            var start = System.currentTimeMillis()
            val gsonStr = gson.toJson(list)
            System.out.println("=============>j gson ser take:" + (System.currentTimeMillis() - start))

            start = System.currentTimeMillis()
            val type: Type = Types.newParameterizedType(
                MutableList::class.java,
                InnerDto2::class.java
            )
            val adapter: com.squareup.moshi.JsonAdapter<List<InnerDto2>> =
                moshi.adapter<List<InnerDto2>>(type)
            val moshiStr = adapter.toJson(list)
            System.out.println("=============>j moshi ser take:" + (System.currentTimeMillis() - start))

            start = System.currentTimeMillis()
            val kotlinJson = Json.encodeToString(list)
            System.out.println("=============>j kotlin json ser take:" + (System.currentTimeMillis() - start))


            start = System.currentTimeMillis()
            val protobufData = kotlinx.serialization.protobuf.ProtoBuf.encodeToByteArray(list)
            System.out.println("=============>j kotlin protobuf ser take:" + (System.currentTimeMillis() - start))

            start = System.currentTimeMillis()
            val hexData = kotlinx.serialization.protobuf.ProtoBuf.encodeToHexString(list)
            System.out.println("=============>j kotlin hex ser take:" + (System.currentTimeMillis() - start))



            start = System.currentTimeMillis()
            val cborSerlized = Cbor.encodeToByteArray(list)
            System.out.println("=============>j kotlin cbor ser take:" + (System.currentTimeMillis() - start))




            start = System.currentTimeMillis()


            val deserializerMoshi: com.squareup.moshi.JsonAdapter<List<InnerDto2>> =
                moshi.adapter<List<InnerDto2>>(
                    Types.newParameterizedType(
                        List::class.java,
                        InnerDto2::class.java
                    )
                )
            start = System.currentTimeMillis()
            val fromMoshi = deserializerMoshi.fromJson(moshiStr)
            System.out.println("=============>j moshi deser take:" + (System.currentTimeMillis() - start))


            start = System.currentTimeMillis()
            val fromKotlin = Json.decodeFromString<List<InnerDto2>>(kotlinJson)
            System.out.println("=============>j kotlin json deser take:" + (System.currentTimeMillis() - start))

            start = System.currentTimeMillis()
            val decodeProto =
                kotlinx.serialization.protobuf.ProtoBuf.decodeFromByteArray<List<InnerDto2>>(
                    protobufData
                )
            System.out.println("=============>j kotlin protobuf deser take:" + (System.currentTimeMillis() - start))


            start = System.currentTimeMillis()
            val decodeHex =
                kotlinx.serialization.protobuf.ProtoBuf.decodeFromHexString<List<InnerDto2>>(hexData)
            System.out.println("=============>j kotlin hex deser take:" + (System.currentTimeMillis() - start))


            start = System.currentTimeMillis()
            val decodeCbor = Cbor.decodeFromByteArray<List<InnerDto2>>(cborSerlized)
            System.out.println("=============>j kotlin cbor deser take:" + (System.currentTimeMillis() - start))

        }).start()
    }

    fun testJson2() {
        Thread(Runnable {
            val moshi: Moshi = Moshi.Builder()
                .addLast(KotlinJsonAdapterFactory())
                .build()
            val gson = Gson()
            val count = 1000;
            val childCount = 0;

            val apply = InnerDto2().apply {
                name = "xxxxxx";
                des = "ehgfggds";
//                this.subNodes = mutableListOf<String>().apply {
//                    for (i in 0..childCount) {
//                        this.add("${i}")
//                    }
//                }
//                this.mp = mutableMapOf<String,String>().apply {
//                    for (i in 0..childCount) {
//                        this.put("${i}","${i}")
//                    }
//                }
            }

            var start = System.currentTimeMillis()
            for (i in 0..count) {
                val gsonStr = gson.toJson(apply)
            }
            System.out.println("=============>j2 gson ser take:" + (System.currentTimeMillis() - start))

            start = System.currentTimeMillis()
            val adapter: com.squareup.moshi.JsonAdapter<InnerDto2> =
                moshi.adapter<InnerDto2>(InnerDto2::class.java)
            for (i in 0..count) {
                val moshiStr = adapter.toJson(apply)
            }
            System.out.println("=============>j2 moshi ser take:" + (System.currentTimeMillis() - start))


            val gsonStr = gson.toJson(apply)
            start = System.currentTimeMillis()
            for (i in 0..count) {
                val fromJson = gson.fromJson<InnerDto2>(gsonStr, InnerDto2::class.java)
            }
            System.out.println("=============>j2 gson deser take:" + (System.currentTimeMillis() - start))


            start = System.currentTimeMillis()
            for (i in 0..count) {
                val fromJson = adapter.fromJson(gsonStr)
            }
            System.out.println("=============>j2 moshi deser take:" + (System.currentTimeMillis() - start))

        })
            .start()

    }
}