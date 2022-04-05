package com.xxf.objectbox.demo

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.xxf.objectbox.demo.UserDbService.clearTable
import com.xxf.objectbox.demo.UserDbService.addAll
import com.xxf.objectbox.demo.UserDbService.queryAll
import com.xxf.objectbox.demo.UserDbService.query
import com.xxf.objectbox.demo.TeacherDbService.add
import com.xxf.objectbox.demo.TeacherDbService.queryAll
import androidx.appcompat.app.AppCompatActivity
import com.xxf.objectbox.ObjectBoxFactory
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import kotlin.Throws
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.core.ObservableSource
import com.xxf.objectbox.demo.UserDbService
import com.xxf.objectbox.demo.TeacherDbService
import com.xxf.objectbox.demo.model.*
import com.xxf.objectbox.observable
import com.xxf.objectbox.observableChange
import com.xxf.rxjava.bindLifecycle
import com.xxf.rxjava.observeOnIO
import com.xxf.rxjava.observeOnMain
import io.objectbox.Box
import io.objectbox.BoxStore
import io.objectbox.Property
import io.objectbox.query.QueryBuilder
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.BooleanSupplier
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.functions.Function
import io.reactivex.rxjava3.functions.Supplier
import io.reactivex.rxjava3.schedulers.Schedulers
import java.lang.Exception
import java.lang.RuntimeException
import java.util.*
import java.util.concurrent.Callable
import kotlin.random.Random

class MainActivity() : AppCompatActivity() {
    var i: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        RxJavaPlugins.setErrorHandler { }

        val box = getBox(this)
        val findViewById = findViewById<Button>(R.id.test)
        box.closeThreadResources()
        findViewById.setOnClickListener {
            val nextInt = Random.nextInt(100)
            box
                .boxFor(Teacher::class.java)
                .put(Teacher(System.currentTimeMillis(), "t_" + System.currentTimeMillis(), age =nextInt))
            System.out.println("====================>insert :${nextInt}")
            Toast.makeText(it.context, "插入了", Toast.LENGTH_SHORT).show()
        }
        box
            .boxFor(Teacher::class.java)
            .query()
            .lessOrEqual(Teacher_.age,20)
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
        testNoEquals()
        i = 0
        Observable
            .fromCallable(object : Callable<Long> {
                @Throws(Exception::class)
                override fun call(): Long {
                    Thread.sleep(1000)
                    i++
                    Log.d("====>", "=============>发送成功:$i")
                    //                        if (i >= 16) {
//                            throw new RuntimeException("xxxx");
//                        }
                    return i
                }
            })
            .subscribeOn(Schedulers.io())
            .repeatUntil(object : BooleanSupplier {
                @Throws(Throwable::class)
                override fun getAsBoolean(): Boolean {
                    return false
                }
            })
            .onErrorComplete()
            .subscribeOn(Schedulers.io())
            .flatMap(object : Function<Long?, ObservableSource<Long>?> {
                @Throws(Throwable::class)
                override fun apply(aLong: Long?): ObservableSource<Long>? {
                    return null
                }
            })
            .map(object : Function<Long, Long> {
                @Throws(Throwable::class)
                override fun apply(o: Long): Long {
                    Log.d("====>", "=============>查询成功:$o")
                    Thread.sleep(1000)
                    if (o > 10) {
                        throw RuntimeException("XXX")
                    }
                    return o
                }
            })
            .doOnError(object : Consumer<Throwable?> {
                @Throws(Throwable::class)
                override fun accept(throwable: Throwable?) {
                    Log.d("====>", "=============>重试失败")
                }
            }).subscribeOn(Schedulers.io())
            .subscribe(object : Consumer<Long?> {
                @Throws(Throwable::class)
                override fun accept(aLong: Long?) {
                    //Log.d("====>", "=============>查询成功2:" + aLong);
                }
            })
        try {
            clearTable(this)
            addAll(
                this, Arrays.asList(
                    User(0, "张三", 10),
                    User(0, "李四", 10),
                    User(0, "王五", 10),
                    User(0, "甲6", 10)
                )
            )
            val users: List<User> = UserDbService.queryAll(this)
            Log.d("=======>", "query:$users")
        } catch (e: Throwable) {
            Log.d("=======>", "error:$e")
        }
        test()
        test2()
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
        Thread(object : Runnable {
            override fun run() {
                clearTable(this@MainActivity)
                val users: MutableList<User> = ArrayList()
                for (i in 0..9999) {
                    users.add(User(0, "李四:$i", i))
                }
                addAll(this@MainActivity, users)
                val start = System.currentTimeMillis()
                query(this@MainActivity, "李")
                Log.d("====> objectbox:", "" + (System.currentTimeMillis() - start))
            }
        }).start()
    }

    private fun test2() {
        val aa = Teacher(1001, "aa")
        add(this, Arrays.asList(aa))
        aa.id = 1002
        add(this, Arrays.asList(aa))
        val teachers = TeacherDbService.queryAll(this)
        Log.d("====>teachers:", "" + teachers)
    }

    private fun getBox(context: Context): BoxStore {
        return ObjectBoxFactory.getBoxStore(
            context.applicationContext!! as Application,
            MyObjectBox.builder(),
            "test3.ob"
        )!!;
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