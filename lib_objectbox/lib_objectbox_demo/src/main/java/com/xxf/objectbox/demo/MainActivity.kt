package com.xxf.objectbox.demo

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.KeyboardShortcutGroup
import android.view.Menu
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.xxf.objectbox.demo.TeacherDbService.add
import androidx.appcompat.app.AppCompatActivity
import com.xxf.objectbox.*
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.disposables.Disposable
import com.xxf.objectbox.demo.model.*
import com.xxf.objectbox.demo.nested.TestPODbService
import com.xxf.rxjava.*
import io.objectbox.BoxStore
import io.objectbox.query.QueryBuilder
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*
import kotlin.random.Random

class MainActivity() : AppCompatActivity() {
    var i: Long = 0
    var tv: TextView? = null

    val testPODbService by lazy {
        TestPODbService()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        RxJavaPlugins.setErrorHandler { }
        tv = findViewById(R.id.test2)

        val box = getBox(this)
        val findViewById = findViewById<Button>(R.id.test)
        findViewById.setOnClickListener {
            val nextInt = Random.nextInt(100)
            box
                .boxFor(Teacher::class.java)
                .put(
                    Teacher(
                        System.currentTimeMillis(),
                        "t_" + System.currentTimeMillis(),
                        age = nextInt,
                        user = Teacher.Student()
                    )
                )
            System.out.println("====================>insert :${nextInt}")
            Toast.makeText(it.context, "插入了", Toast.LENGTH_SHORT).show()

//            testPODbService.testAdd()
//            testPODbService.testQuery()
        }
        box
            .boxFor(Teacher::class.java)
            .query()
            .lessOrEqual(Teacher_.age, 20)
            .build()
            .observableChange()
            .observeOnMain()
            .bindLifecycle(this)
            .subscribe {
                System.out.println("====================>it:${it.size}")
            }
    }

    var subscribe: Disposable? = null
    override fun onResume() {
        super.onResume()
        i = 0
        // test()

        // insert()
       // testSpeed()
        testCountSpeed();
    }


    private fun testNoEquals() {
        Thread(Runnable {
            val boxFor = getBox(this).boxFor(Animal::class.java)
            val cunt = 100000;
            for (index in 1..cunt) {
                val put = boxFor.put(Animal().apply {
                    this.uuid = "x_$index"
                    this.name = "name_" + index
                })
            }
            var start = System.currentTimeMillis()
            var query = boxFor.query()
            for (index in 1..cunt - 25) {
                val name = "name_" + index
                query = query.notEqual(Animal_.name, name, QueryBuilder.StringOrder.CASE_SENSITIVE)
            }
            System.out.println("=================>query take1:" + (System.currentTimeMillis() - start))


            System.out.println("=================>query take1:" + (System.currentTimeMillis() - start))
            System.out.println(
                "=================>query:" + query
                    .order(Animal_.name)
                    .build().find().apply {
                        System.out.println("================>query size:" + this.size)
                    }
            )
        })
            .start()

    }

    private fun test() {
        for (i in 0..100) {
            val box = getBox(this)
            Observable.just(1)
                .subscribeOn(Schedulers.io())
                .map {
                    Thread.sleep(Random.nextLong(10000))
                    val all = box.boxFor(Teacher::class.java).query().build().find()

                    System.out.println("===========>测试一个线程:" + all.size + "  " + Thread.currentThread().name)
                    val find = box.boxFor(Teacher::class.java).query().build().find(0, 1)
                    System.out.println("===========>测试一个线程2:" + find.size + "  " + Thread.currentThread().name)
                    true
                }.doOnError {
                    System.out.println("===========>测试一个线程:异常了:" + it + "  " + Thread.currentThread().name)
                }
                .subscribe()
        }
//        Thread(object : Runnable {
//            override fun run() {
//                //clearTable(this@MainActivity)
//                val users: MutableList<User> = ArrayList()
//                for (i in 0..9999) {
//                    users.add(User(0, "李四:$i", i))
//                }
//                addAll(this@MainActivity, users)
//                val start = System.currentTimeMillis()
//                query(this@MainActivity, "李")
//                Log.d("====> objectbox:", "" + (System.currentTimeMillis() - start))
//            }
//        }).start()
    }

    private fun test2() {
        val aa = Teacher(1001, "aa", user = Teacher.Student())
        add(this, Arrays.asList(aa))
        aa.id = 1002
        add(this, Arrays.asList(aa))
        val teachers = TeacherDbService.queryAll(this)
        Log.d("====>teachers:", "" + teachers)
    }

    private fun getBox(context: Context): BoxStore {
        return MyObjectBox.builder().maxReaders(256)
            //.noReaderThreadLocals()
            .queryAttempts(4)
            .buildSingle("test33.ob", allowMainThreadOperation = true)
    }

    private fun getBox4(context: Context): BoxStore {
        return MyObjectBox.builder().maxReaders(256)
            //.noReaderThreadLocals()
            .queryAttempts(4)
            .buildSingle("test44.ob", allowMainThreadOperation = true)
    }

    private fun insert() {
        val box = getBox(this)
        for (i in 0..20L) {
            try {
                box.boxFor(Teacher::class.java).put(
                    Teacher(System.currentTimeMillis(), "" + i, user = Teacher.Student())
                )
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
        val list = longArrayOf(4, 2, 2, 5)
        val find = box.boxFor(Teacher::class.java).query().`in`(Teacher_.id, list).build().find()
        System.out.println("===============>find:" + find.map { it.id })

        val listName = arrayOf<String>("4", "2", "5")
        val find2 = box.boxFor(Teacher::class.java).query()
            .`in`(Teacher_.name, listName, QueryBuilder.StringOrder.CASE_SENSITIVE).build().find()
            .toKeySortList(listName, {
                it.name
            })
        System.out.println("===============>find2:" + find2.map { it.name })
        val find3 = box.boxFor(Teacher::class.java).query()
            .`in`(Teacher_.name, listName, QueryBuilder.StringOrder.CASE_SENSITIVE).build().find()
            .toKeySortMap(listName, {
                it.name
            })
        System.out.println("===============>find3:" + find3.map { it.value })
    }

    private fun testSpeed() {
        Observable.fromCallable {
            var start = System.currentTimeMillis()
            val box = getBox(this)
//            for (i in 1..1000) {
//                box.boxFor(Teacher::class.java).put(
//                    Teacher(i.toLong(), "" + i, user = Teacher.Student())
//                )
//            }
            val list = mutableListOf<Long>()
            for (i in 1..100L) {
                list.add(i)
            }
            val boxFor = box.boxFor(Teacher::class.java)
            start = System.currentTimeMillis()
            for (i in 0..10000) {
                val get2 = boxFor.getSafe(list.first())
            }
            val take2 = System.currentTimeMillis() - start


            start = System.currentTimeMillis()
            for (i in 0..10000) {
                val get = boxFor.get(list.first())
            }
            val take1 = System.currentTimeMillis() - start

            start = System.currentTimeMillis()
            for (i in 0..10000) {
                val get = boxFor.get(list.first())
            }
            val take3 = System.currentTimeMillis() - start

            val thread=Thread.currentThread().name


            Handler(Looper.getMainLooper()).post {
                tv?.text = "${take1}  ${take2} ${take3}  ${thread}"
            }
            true
        }.repeat(100)
            .subscribeOnIO()
            .subscribe()
    }

    private fun testCountSpeed(){
        val box = getBox(this).boxFor(Animal::class.java);
        box.removeAll();
        for(i in 1..1000){
            box.put(Animal().apply {
                this.name = "李四"
                this.uuid = ""+i;
            })
        }

        var start = System.nanoTime();
        box.query()
            .`in`(Animal_.uuid, arrayOf("500","600","700"),QueryBuilder.StringOrder.CASE_INSENSITIVE)
            .build()
            .count();
        var end=System.nanoTime();
        System.out.println("=============>take2:" +(end-start));


        start = System.nanoTime();
        box.query()
            .`in`(Animal_.uuid, arrayOf("500","600","700"),QueryBuilder.StringOrder.CASE_INSENSITIVE)
            .build().findFirst();
        end=System.nanoTime();
        System.out.println("=============>take3:" +(end-start));

        start = System.nanoTime();
        box.query()
            .`in`(Animal_.uuid, arrayOf("500","600","700"),QueryBuilder.StringOrder.CASE_INSENSITIVE)
            .build().find().size;
        end=System.nanoTime();
        System.out.println("=============>take4:" +(end-start));


        start = System.nanoTime();
        box.query()
            .`in`(Animal_.uuid, arrayOf("500","600","700"),QueryBuilder.StringOrder.CASE_INSENSITIVE)
            .build().findIds().size
        end=System.nanoTime();
        System.out.println("=============>take5:" +(end-start));

        start = System.nanoTime();
        box.query()
            .`in`(Animal_.uuid, arrayOf("500","600","700"),QueryBuilder.StringOrder.CASE_INSENSITIVE)
            .build().findIds(0,1);
        end=System.nanoTime();
        System.out.println("=============>take6:" +(end-start));


        start = System.nanoTime();
       val result= box.query()
            .`in`(Animal_.uuid, arrayOf("500","600","700"),QueryBuilder.StringOrder.CASE_INSENSITIVE)
            .build().hasResult();
        end=System.nanoTime();
        System.out.println("=============>take7:" +(end-start)+" res:"+result);
    }
    private fun testUnique() {

        getBox(this).boxFor(Animal::class.java)
            .put(
                listOf(
                    Animal().apply {
                        this.name = "张三"
                        this.uuid = "1"
                    },
                    Animal().apply {
                        this.name = "李四"
                        this.uuid = "2"
                    })
            )
        val find = getBox(this).boxFor(Animal::class.java).query().build().find()
        System.out.println("=============>query result:" + find)

        getBox(this).boxFor(Animal::class.java)
            .put(
                listOf(
                    Animal().apply {
                        this.name = "张三2"
                        this.uuid = "1"
                    },
                    Animal().apply {
                        this.name = "李四2"
                        this.uuid = "2"
                    })
            )
        val find2 = getBox(this).boxFor(Animal::class.java).query().build().find()
        System.out.println("=============>query result2:" + find2)

        listOf("x").firstOrNull()
    }

    override fun onProvideKeyboardShortcuts(
        data: MutableList<KeyboardShortcutGroup>?,
        menu: Menu?,
        deviceId: Int
    ) {
        super.onProvideKeyboardShortcuts(data, menu, deviceId)
    }

}