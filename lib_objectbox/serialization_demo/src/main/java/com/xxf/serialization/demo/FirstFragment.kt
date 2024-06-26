package com.xxf.serialization.demo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bluelinelabs.logansquare.LoganSquare
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.twitter.serial.serializer.CollectionSerializers
import com.twitter.serial.stream.bytebuffer.ByteBufferSerial
import com.xxf.serialization.demo.databinding.FragmentFirstBinding
import com.xxf.serialization.demo.model.ExampleObject
import com.xxf.serialization.demo.model.ExampleObject3
import com.xxf.serialization.demo.model.User
import java.io.ByteArrayOutputStream
import java.io.ObjectOutputStream
import java.io.ByteArrayInputStream
import java.io.ObjectInputStream
import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.Input
import com.esotericsoftware.kryo.io.Output
import com.xxf.serialization.demo.model.cryo.SimpleModel
import com.xxf.serialization.demo.model.cryo.UserSchema
import io.protostuff.Schema
import io.protostuff.runtime.RuntimeSchema
import io.protostuff.LinkedBuffer
import io.protostuff.ProtostuffIOUtil


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        //test()
        test2()
        test3()
        test4()
        testSimpleModelByGson()
        testSimpleModelByKryo()
        testSimpleModelByParcelable()
        testSimpleModelByProtostuff()
        testUserModelByProtostuff()
        testUserModelByLoganSquare()
    }

    val list_size = 1000
    val subnodes_size = 500
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun test() {
        Thread(Runnable {

            var start = System.currentTimeMillis()
            val byt = ByteArrayOutputStream()
            for (i in 0..list_size) {
                try {
                    val oos = ObjectOutputStream(byt);
                    oos.writeObject(User().apply {
                        this.name = "xx${i}"
                        this.age = i;
                        val subnodes = mutableListOf<String>()
                        val mapTets = mutableMapOf<String, String>()
                        for (j in 0..subnodes_size) {
                            subnodes.add("${j}")
                            mapTets.put("${j}", "${j}")
                        }
                        this.subNodes = subnodes
                        this.map = mapTets
                    });
                    oos.close();
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            System.out.println("=============>take java ser:${System.currentTimeMillis() - start}")

            start = System.currentTimeMillis()
            var stu2: User? = null
            for (i in 0..list_size) {
                try {
                    val byteInt = ByteArrayInputStream(byt.toByteArray())
                    val objInt = ObjectInputStream(byteInt)
                    stu2 = objInt.readObject() as User
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            val take = System.currentTimeMillis() - start
            System.out.println("=============>take java der:${take}  $stu2")

        }).start()
    }

    private fun test2() {
        Thread(Runnable {
            Thread.sleep(500)
            val gson = Gson()
            var start = System.currentTimeMillis()
            var byt: String = ""
            val list = mutableListOf<ExampleObject>()
            for (i in 0..list_size) {
                list.add(ExampleObject().apply {
                    this.name = "xx${i}"
                    this.age = i;
                    val subnodes = mutableListOf<String>()
                    val mapTets = mutableMapOf<String, String>()
                    for (j in 0..subnodes_size) {
                        subnodes.add("${j}")
                        mapTets.put("${j}", "${j}")
                    }
                    this.subNodes = subnodes
                    // this.map = mapTets
                })
            }
            start = System.currentTimeMillis()
            byt = gson.toJson(list)
            System.out.println("=============>take gson ser:${System.currentTimeMillis() - start}")

            start = System.currentTimeMillis()
            var stu2: List<ExampleObject>? =
                gson.fromJson(byt, object : TypeToken<List<ExampleObject>>() {}.type)
            val take = System.currentTimeMillis() - start
            System.out.println("=============>take gson der:${take} ${stu2}")

        }).start()
    }

    private fun test3() {
        Thread(Runnable {
            Thread.sleep(1000)
            var start = System.currentTimeMillis()
            var serializedData: ByteArray = byteArrayOf()
            val list = mutableListOf<ExampleObject>()
            for (i in 0..list_size) {
                list.add(ExampleObject().apply {
                    this.name = "xx${i}"
                    this.age = i;
                    val subnodes = mutableListOf<String>()
                    val mapTets = mutableMapOf<String, String>()
                    for (j in 0..subnodes_size) {
                        subnodes.add("${j}")
                        mapTets.put("${j}", "${j}")
                    }
                    this.subNodes = subnodes
                    // this.map = mapTets
                })
//
//                list.add(ExampleObject().apply {
//                    this.name = "xx${i}"
//                    this.age = i;
//                })
            }

            val serial = ByteBufferSerial();
            start = System.currentTimeMillis()
            serializedData =
                serial.toByteArray(
                    list,
                    CollectionSerializers.getListSerializer(ExampleObject.BIN_SERIALIZER)
                )
            System.out.println("=============>take Serial  ser:${System.currentTimeMillis() - start}")

            start = System.currentTimeMillis()
            var serialDerResult: List<ExampleObject>? = serial.fromByteArray(
                serializedData,
                CollectionSerializers.getListSerializer(ExampleObject.BIN_SERIALIZER)
            )
            val take = System.currentTimeMillis() - start
            System.out.println("=============>take Serial  der:${take}   ${serialDerResult}")
        }).start()
    }

    private fun test4() {
        Thread(Runnable {
            Thread.sleep(1500)
            var start = System.currentTimeMillis()
            var serializedData: ByteArray = byteArrayOf()
            val list: MutableList<ExampleObject3> = mutableListOf<ExampleObject3>()
            for (i in 0..list_size) {
                list.add(ExampleObject3().apply {
                    this.name = "xx${i}"
                    this.age = i;
                    val subnodes = mutableListOf<String>()
                    val mapTets = mutableMapOf<String, String>()
                    for (j in 0..subnodes_size) {
                        subnodes.add("${j}")
                        mapTets.put("${j}", "${j}")
                    }
                    this.subNodes = subnodes
                    // this.map = mapTets
                })
//
//                list.add(ExampleObject().apply {
//                    this.name = "xx${i}"
//                    this.age = i;
//                })
            }

            start = System.currentTimeMillis()
            serializedData = ParcelableUtil.marshall(list)
            System.out.println("=============>take Parcelize  ser:${System.currentTimeMillis() - start}")

            start = System.currentTimeMillis()
            var serialDerResult: List<ExampleObject3>? =
                ParcelableUtil.unmarshallList(serializedData)
            val take = System.currentTimeMillis() - start
            System.out.println("=============>take Parcelize  der:${take}   ${serialDerResult}")
        }).start()
    }

    private fun testSimpleModelByKryo() {
        Thread(Runnable {
            //Thread.sleep(2500)
            val kryo = Kryo()
            kryo.register(SimpleModel::class.java)
            kryo.register(java.util.ArrayList::class.java)
            kryo.register(SimpleModel.Type::class.java)

            var start = System.currentTimeMillis()
            var byt: ByteArray? = null
            start = System.currentTimeMillis()


            for (i in 0..list_size) {
                val output = Output(1024,-1)
                kryo.writeObject(output, SimpleModel().apply {
                    this.name = "name_${i}"
                    this.age = i
                    val mutableListOf = mutableListOf<String>()
                    for (j in 0..subnodes_size) {
                      mutableListOf.add("${j}")
                    }
                    this.subNode =mutableListOf
                })
                byt=output.buffer
                output.close()
            }
            System.out.println("=============>take simple Kryo  ser:${System.currentTimeMillis() - start}")

            start = System.currentTimeMillis()
            var serialDerResult: SimpleModel? = null
            for (i in 0..list_size) {
                val input = Input(byt)
                serialDerResult = kryo.readObject(input, SimpleModel::class.java)
            }
            val take = System.currentTimeMillis() - start
            System.out.println("=============>take simple Kryo  der:${take}   ${serialDerResult}")
        }).start()
    }

    private fun testSimpleModelByGson() {
        Thread(Runnable {
            Thread.sleep(500)
            val gson = Gson()
            var start = System.currentTimeMillis()
            var byt: String? = null
            start = System.currentTimeMillis()


            for (i in 0..list_size) {
                byt = gson.toJson(SimpleModel().apply {
                    this.name = "name_${i}"
                    this.age = i
                    val mutableListOf = mutableListOf<String>()
                    for (j in 0..subnodes_size) {
                        mutableListOf.add("${j}")
                    }
                    this.subNode =mutableListOf
                })
            }
            System.out.println("=============>take simple gson  ser:${System.currentTimeMillis() - start}")

            start = System.currentTimeMillis()
            var serialDerResult: SimpleModel? = null
            for (i in 0..list_size) {
                serialDerResult = gson.fromJson(byt, SimpleModel::class.java)
            }
            val take = System.currentTimeMillis() - start
            System.out.println("=============>take simple gson  der:${take}   ${serialDerResult}")
        }).start()
    }

    private fun testSimpleModelByParcelable() {
        Thread(Runnable {
            Thread.sleep(1000)
            var start = System.currentTimeMillis()
            var byt: ByteArray? = null
            start = System.currentTimeMillis()


            for (i in 0..list_size) {
                byt = ParcelableUtil.marshall(SimpleModel().apply {
                    this.name = "name_${i}"
                    this.age = i
                    val mutableListOf = mutableListOf<String>()
                    for (j in 0..subnodes_size) {
                        mutableListOf.add("${j}")
                    }
                    this.subNode =mutableListOf
                })
            }
            System.out.println("=============>take simple Parcelable  ser:${System.currentTimeMillis() - start}")

            start = System.currentTimeMillis()
            var serialDerResult: SimpleModel? = null
            for (i in 0..list_size) {
                serialDerResult = ParcelableUtil.unmarshall(byt, SimpleModel.CREATOR)
            }
            val take = System.currentTimeMillis() - start
            System.out.println("=============>take simple Parcelable  der:${take}   ${serialDerResult}")
        }).start()
    }
    private fun testSimpleModelByProtostuff() {
        Thread(Runnable {
            Thread.sleep(1500)
            val schema: Schema<SimpleModel> = RuntimeSchema.getSchema(SimpleModel::class.java)

            var start = System.currentTimeMillis()
            var byt: ByteArray? = null
            start = System.currentTimeMillis()


            for (i in 0..list_size) {
                val buffer = LinkedBuffer.allocate(512)
                byt = ProtostuffIOUtil.toByteArray(SimpleModel().apply {
                    this.name = "name_${i}"
                    this.age = i
                    val mutableListOf = mutableListOf<String>()
                    for (j in 0..subnodes_size) {
                        mutableListOf.add("${j}")
                    }
                    this.subNode =mutableListOf
                }, schema, buffer);
                buffer.clear()
            }
            System.out.println("=============>take simple Protostuff  ser:${System.currentTimeMillis() - start}")

            start = System.currentTimeMillis()
            var serialDerResult: SimpleModel? = schema.newMessage()
            for (i in 0..list_size) {
                ProtostuffIOUtil.mergeFrom(byt, serialDerResult, schema);
            }
            val take = System.currentTimeMillis() - start
            System.out.println("=============>take simple Protostuff  der:${take}   ${serialDerResult}")
        }).start()
    }

    private fun testUserModelByProtostuff() {
        Thread(Runnable {
            Thread.sleep(1800)
            val schema: UserSchema = UserSchema()

            var start = System.currentTimeMillis()
            var byt: ByteArray? = null
            start = System.currentTimeMillis()


            for (i in 0..list_size) {
                val buffer = LinkedBuffer.allocate(512)
                byt = ProtostuffIOUtil.toByteArray(com.xxf.serialization.demo.model.cryo.User().apply {
                    this.firstName = "name_${i}"
                    this.lastName = i.toString()
                    this.email=i.toString()
                    val mutableListOf = mutableListOf<String>()
                    for (j in 0..subnodes_size) {
                        mutableListOf.add("${j}")
                    }
                    this.subNode =mutableListOf
                }, schema, buffer);
                buffer.clear()
            }
            System.out.println("=============>take simple Protostuff2  ser:${System.currentTimeMillis() - start}")

            start = System.currentTimeMillis()
            var serialDerResult: com.xxf.serialization.demo.model.cryo.User? = schema.newMessage()
            for (i in 0..list_size) {
                ProtostuffIOUtil.mergeFrom(byt, serialDerResult, schema);
            }
            val take = System.currentTimeMillis() - start
            System.out.println("=============>take simple Protostuff2  der:${take}   ${serialDerResult}")
        }).start()
    }

    private fun testUserModelByLoganSquare() {
        Thread(Runnable {
            Thread.sleep(2000)
            var start = System.currentTimeMillis()
            var byt: String? = null
            start = System.currentTimeMillis()


            for (i in 0..list_size) {
                byt =
                    LoganSquare.serialize(com.xxf.serialization.demo.model.cryo.User().apply {
                        this.firstName = "name_${i}"
                        this.lastName = i.toString()
                        this.email = i.toString()
                        val mutableListOf = mutableListOf<String>()
                        for (j in 0..subnodes_size) {
                            mutableListOf.add("${j}")
                        }
                        this.subNode = mutableListOf
                    })
            }
            System.out.println("=============>take simple LoganSquare  ser:${System.currentTimeMillis() - start}")

            start = System.currentTimeMillis()

            var serialDerResult: com.xxf.serialization.demo.model.cryo.User?=null
            for (i in 0..list_size) {
               serialDerResult= LoganSquare.parse(byt,com.xxf.serialization.demo.model.cryo.User::class.java)
            }
            val take = System.currentTimeMillis() - start
            System.out.println("=============>take simple LoganSquare  der:${take}   ${serialDerResult}")
        }).start()
    }
}