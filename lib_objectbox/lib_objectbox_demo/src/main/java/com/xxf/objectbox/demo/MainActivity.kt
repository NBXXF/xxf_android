package com.xxf.objectbox.demo

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.xxf.objectbox.demo.TeacherDbService.add
import androidx.appcompat.app.AppCompatActivity
import com.xxf.objectbox.buildSingle
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.disposables.Disposable
import com.xxf.objectbox.demo.model.*
import com.xxf.objectbox.toKeySortList
import com.xxf.objectbox.observableChange
import com.xxf.objectbox.toKeySortMap
import com.xxf.rxjava.*
import io.objectbox.BoxStore
import io.objectbox.query.QueryBuilder
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*
import kotlin.random.Random

class MainActivity() : AppCompatActivity() {
    var i: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        RxJavaPlugins.setErrorHandler { }

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

        insert()
        testSpeed()
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
            .buildSingle( "test33.ob",allowMainThreadOperation = true)
    }

    private fun getBox4(context: Context): BoxStore {
            return MyObjectBox.builder().maxReaders(256)
                //.noReaderThreadLocals()
                .queryAttempts(4)
                .buildSingle( "test44.ob",allowMainThreadOperation = true)
    }

    private fun insert() {
        val box = getBox(this)
        for (i in 0..20L) {
            try {
                box.boxFor(Teacher::class.java).put(
                    Teacher(System.currentTimeMillis(), "" + i,user=Teacher.Student())
                )
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
        val list = longArrayOf(4, 2, 2, 5)
        val find = box.boxFor(Teacher::class.java).query().`in`(Teacher_.id, list).build().find()
        System.out.println("===============>find:" + find.map { it.id })

        val listName = arrayOf<String>("4", "2", "5")
        val find2 = box.boxFor(Teacher::class.java).query().`in`(Teacher_.name, listName,QueryBuilder.StringOrder.CASE_SENSITIVE).build().find()
            .toKeySortList(listName,{
            it.name
        })
        System.out.println("===============>find2:" + find2.map { it.name })
        val find3 = box.boxFor(Teacher::class.java).query().`in`(Teacher_.name, listName,QueryBuilder.StringOrder.CASE_SENSITIVE).build().find()
            .toKeySortMap(listName,{
                it.name
            })
        System.out.println("===============>find3:" + find3.map { it.value })
    }

    private fun testSpeed() {
        Thread(Runnable {
            var start = System.currentTimeMillis()
            val box = getBox(this)
            start = System.currentTimeMillis()
            val find = box.boxFor(Teacher::class.java).query().build().find()
            System.out.println("================>take:" + (System.currentTimeMillis() - start) + "  size:" + find.size)

            val box4 = getBox4(this)
            start = System.currentTimeMillis()
            val find1 = box4.boxFor(Teacher::class.java).query().build().find()
            System.out.println("================>take2:" + (System.currentTimeMillis() - start) + "  size:" + find1.size)


        }).start()
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
    }

}