package com.xxf.serialization.demo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.twitter.serial.serializer.CollectionSerializers
import com.twitter.serial.stream.bytebuffer.ByteBufferSerial
import com.xxf.serialization.demo.databinding.FragmentFirstBinding
import com.xxf.serialization.demo.model.ExampleObject
import com.xxf.serialization.demo.model.User
import java.io.ByteArrayOutputStream
import java.io.ObjectOutputStream
import java.io.ByteArrayInputStream
import java.io.ObjectInputStream


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
    }

    val retry_time = 10000
    val subnodes_size = 5
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun test() {
        Thread(Runnable {

            var start = System.currentTimeMillis()
            val byt = ByteArrayOutputStream()
            for (i in 0..retry_time) {
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
            for (i in 0..retry_time) {
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
            val gson = Gson()
            var start = System.currentTimeMillis()
            var byt: String = ""
            val list = mutableListOf<ExampleObject>()
            for (i in 0..retry_time) {
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
                    this.map = mapTets
                })
            }
            start = System.currentTimeMillis()
            byt = gson.toJson(list)
            System.out.println("=============>take gson ser:${System.currentTimeMillis() - start}")

            start = System.currentTimeMillis()
            var stu2: List<ExampleObject>? = gson.fromJson(byt, object : TypeToken<List<ExampleObject>>() {}.type)
            val take = System.currentTimeMillis() - start
            System.out.println("=============>take gson der:${take} ${stu2}")

        }).start()
    }

    private fun test3() {
        Thread(Runnable {
            var start = System.currentTimeMillis()
            var serializedData: ByteArray = byteArrayOf()
            val list = mutableListOf<ExampleObject>()
            for (i in 0..retry_time) {
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
                    this.map = mapTets
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
}