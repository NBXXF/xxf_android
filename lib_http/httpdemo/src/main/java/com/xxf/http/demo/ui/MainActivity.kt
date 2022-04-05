package com.xxf.http.demo.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import com.google.gson.*
import com.google.gson.annotations.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.xxf.arch.apiService
import com.xxf.arch.json.JsonUtils
import com.xxf.arch.json.ListTypeToken
import com.xxf.arch.json.typeadapter.NullableSerializerTypeAdapterFactory
import com.xxf.arch.utils.copy
import com.xxf.arch.websocket.WebSocketClient
import com.xxf.http.demo.*
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject
import kotlinx.serialization.*
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import retrofit2.CacheType
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit
import kotlin.random.Random
import kotlin.reflect.jvm.internal.impl.metadata.ProtoBuf

class MainActivity : AppCompatActivity() {
    class Demo {
        @JsonAdapter(NullableSerializerTypeAdapterFactory::class, nullSafe = false)
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
        @JsonAdapter(NullableSerializerTypeAdapterFactory::class, nullSafe = false)
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
        RxJavaPlugins.setErrorHandler { }


        val editText = findViewById<MyEditText>(R.id.edit_text)
        val findViewById = findViewById<Switch>(R.id.btn_test)
        findViewById.setOnCheckedChangeListener { buttonView, isChecked ->
            editText.isRead = isChecked
        }


        val map = mutableMapOf<String, Any?>()
        map.put("xxx", "r");
        map.put("xxx2", JsonNull.INSTANCE)
        map.put("xxx3", Demo().apply {
            // this.name="xxx"
        })
        System.out.println("==========>ser:" + JsonUtils.toJsonElement(map))
        System.out.println("==========>ser11:" + JsonUtils.toJsonElement(Demo().apply {
            // this.name="xxx"
        }))
        System.out.println(
            "==========>ser2:" + Gson()
                .newBuilder()
                .serializeNulls()
                .create().toJson(map)
        )
        System.out.println(
            "==========>ser3:" + Gson()
                .newBuilder()
                .serializeNulls()
                .create().toJsonTree(map)
        )

        val testModel = ExposeTestModel().apply {
            this.name = "hello"
            this.des = "x:" + System.currentTimeMillis()
        }
        System.out.println("==========>expose serlize:" + JsonUtils.toJsonString(testModel))

        val copy_1 = testModel.copy()
        System.out.println("==========>expose copy:" + copy_1)

        val copy_2 =
            testModel.copy(excludeUnSerializableField = true, excludeUnDeserializableField = true)
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

    private fun test() {
        val map = mutableMapOf<String, Any?>()
        map.put("xxx", "r");
        map.put("xxx2", JsonNull.INSTANCE)
        map.put("xxx3", Demo().apply {
            // this.name="xxx"
        })
        System.out.println("==========>ser:" + JsonUtils.toJsonElement(map))
        System.out.println("==========>ser11:" + JsonUtils.toJsonElement(Demo().apply {
            // this.name="xxx"
        }, excludeUnSerializableField = true))
        System.out.println(
            "==========>ser2:" + Gson()
                .newBuilder()
                .serializeNulls()
                .create().toJson(map)
        )
        System.out.println(
            "==========>ser3:" + Gson()
                .newBuilder()
                .serializeNulls()
                .create().toJsonTree(map)
        )


        val toJsonString = JsonUtils.toJsonString(Demo2().apply {
            this.name = null
        }, excludeUnSerializableField = false)
        System.out.println("===========>ser4 json:" + toJsonString)
        val toBean = JsonUtils.toBean(
            toJsonString,
            Demo2::class.java,
            excludeUnDeserializableField = false
        )
        System.out.println("===========>ser4:" + toBean)
    }

    override fun onResume() {
        super.onResume()
        testhttp()
        test()
        testJsonSpeed()

        //testJson2()
        Thread(Runnable {
            val list = mutableListOf<StringClass>()
            for (i in 0..100000) {
                list.add(StringClass().apply {
                    this.type = "" + Random.nextInt(20)
                })
            }
            val toJsonString = JsonUtils.toJsonString(list)
            var start = System.currentTimeMillis()
            val toBeanList2 = JsonUtils.toBeanList(toJsonString, StringClass::class.java)
            System.out.println("=============>take enum string:" + (System.currentTimeMillis() - start))

            start = System.currentTimeMillis()
            val toBeanList = JsonUtils.toBeanList(toJsonString, EnumClass::class.java)
            System.out.println("=============>take enum:" + (System.currentTimeMillis() - start))

            start = System.currentTimeMillis()
            for (i in 0..100) {
                JsonUtils.toBeanList(toJsonString, StringClass::class.java)
            }
            System.out.println("=============>take enum string(100000):" + (System.currentTimeMillis() - start))

            start = System.currentTimeMillis()
            for (i in 0..100) {
                JsonUtils.toBeanList(toJsonString, EnumClass::class.java)
            }
            System.out.println("=============>take enum(100000):" + (System.currentTimeMillis() - start))

        }).start()


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
            .getCity(TestQueryJsonField("xxx"))
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                Log.d("==========>retry no", "" + it)
            }
            .subscribe {
                Log.d("==========>retry ye", "" + it)
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
        Observable.interval(5,TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .sample(5, TimeUnit.SECONDS,true)
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
                    this.mp = mutableMapOf<String, String>().apply {
                        put("xx", "21")
                        put("24", "xx")
                    }
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
            val fromJson = gson.fromJson<List<InnerDto2>>(gsonStr, ListTypeToken<InnerDto2>().type)
            System.out.println("=============>j gson deser take:" + (System.currentTimeMillis() - start))


            start = System.currentTimeMillis()
            val deserializerMoshi: com.squareup.moshi.JsonAdapter<List<InnerDto2>> =
                moshi.adapter<List<InnerDto2>>(
                    Types.newParameterizedType(
                        List::class.java,
                        InnerDto2::class.java
                    )
                )
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
        val moshi: Moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
        val gson = Gson()

        val apply = InnerDto2().apply {
            name = "xxxxxx";
            des = "ehgfggds";
        }
        var start = System.currentTimeMillis()
        for (i in 0..100000) {
            val gsonStr = gson.toJson(apply)
        }
        System.out.println("=============>j2 gson ser take:" + (System.currentTimeMillis() - start))

        start = System.currentTimeMillis()
        val adapter: com.squareup.moshi.JsonAdapter<InnerDto2> =
            moshi.adapter<InnerDto2>(InnerDto2::class.java)
        for (i in 0..100000) {
            val moshiStr = adapter.toJson(apply)
        }
        System.out.println("=============>j2 moshi ser take:" + (System.currentTimeMillis() - start))


        val gsonStr = gson.toJson(apply)
        start = System.currentTimeMillis()
        for (i in 0..100000) {
            val fromJson = gson.fromJson<InnerDto2>(gsonStr, InnerDto2::class.java)
        }
        System.out.println("=============>j2 gson deser take:" + (System.currentTimeMillis() - start))


        start = System.currentTimeMillis()
        for (i in 0..100000) {
            val fromJson = adapter.fromJson(gsonStr)
        }
        System.out.println("=============>j2 moshi deser take:" + (System.currentTimeMillis() - start))

    }
}