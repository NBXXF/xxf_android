package com.xxf.objectbox.demo

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
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
import com.xxf.rxjava.observeOnIO
import io.objectbox.Box
import io.objectbox.BoxStore
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

class MainActivity() : AppCompatActivity() {
    var i: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        RxJavaPlugins.setErrorHandler { }
    }

    var subscribe: Disposable? = null
    override fun onResume() {
        super.onResume()
        var aLong: Long
        testUnique()

        Observable.concat(Observable.fromCallable {
            Log.d(
                "====>",
                "=============>result 发送:1" + Thread.currentThread().name
            )
            1
        }, Observable.fromCallable {
            Log.d(
                "====>",
                "=============>result 发送:2" + Thread.currentThread().name
            )
            2
        })
            .take(2)
            .observeOnIO()
            .take(1)
            .subscribe {
                Log.d(
                    "====>",
                    "=============>result:" + it + Thread.currentThread().name
                )
            }
        Observable
            .fromArray(
                Observable.defer(
                    Supplier<ObservableSource<Long>> { Observable.empty() }),
                Observable.fromCallable(object : Callable<Long> {
                    @Throws(Exception::class)
                    override fun call(): Long {
                        Log.d(
                            "====>",
                            "=============>call:1023 start:" + Thread.currentThread().name
                        )
                        Thread.sleep(1000)
                        Log.d("====>", "=============>call:1023 end:" + Thread.currentThread().name)
                        return 1023L
                    }
                }).subscribeOn(Schedulers.io()),
                Observable.fromCallable(object : Callable<Long> {
                    @Throws(Exception::class)
                    override fun call(): Long {
                        Log.d(
                            "====>",
                            "=============>call:1024 start:" + Thread.currentThread().name
                        )
                        //    Thread.sleep(4000)
                        throw  RuntimeException("xxxx");
//                        Log.d("====>", "=============>call:1024 end:" + Thread.currentThread().name)
//                        return 1024L
                    }
                }).subscribeOn(Schedulers.io())
            )
            .concatMapEagerDelayError({ it -> it }, true)
            .doOnError(object : Consumer<Throwable> {
                @Throws(Throwable::class)
                override fun accept(throwable: Throwable) {
                    Log.d(
                        "====>",
                        "=============>call:error:" + throwable + "  t:" + Thread.currentThread().name
                    )
                }
            })
            .subscribe(object : Consumer<Long> {
                @Throws(Throwable::class)
                override fun accept(integer: Long) {
                    Log.d(
                        "====>",
                        "=============>call:recive:" + integer + "  t:" + Thread.currentThread().name
                    )
                    Thread.sleep(2000)
                    //throw RuntimeException("xxxxhgfdfgh")
                }
            })
        //        Observable.concatEagerDelayError(
//                Arrays.asList(Observable.fromCallable(new Callable<Long>() {
//                            @Override
//                            public Long call() throws Exception {
//                                Thread.sleep(1000);
//                                Log.d("====>", "=============>call:1023:" + Thread.currentThread().getName());
//                                return 1023l;
//                            }
//                        }).subscribeOn(Schedulers.io())
//                        , Observable.fromCallable(new Callable<Long>() {
//                            @Override
//                            public Long call() throws Exception {
//                                Thread.sleep(100);
//                                // throw new RuntimeException("xxxx");
//                                Log.d("====>", "=============>call:1024" + Thread.currentThread().getName());
//                                return 1024l;
//                            }
//                        }).subscribeOn(Schedulers.io())
//                ))
//                .subscribeOn(Schedulers.single())
//                .observeOn(Schedulers.newThread())
//                .doOnError(new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Throwable {
//                        Log.d("====>", "=============>call  错误:" + throwable);
//                    }
//                })
//                .subscribe(new Consumer<Long>() {
//                    @Override
//                    public void accept(Long aLong) throws Throwable {
//                        Log.d("====>", "=============>call  收到:" + aLong);
//                    }
//                });
//
//        aLong = Observable.fromCallable(new Callable<Long>() {
//            @Override
//            public Long call() throws Exception {
//                i++;
//                if (i < 8) {
//                    throw new RuntimeException("XXX");
//                }
//                return i;
//            }
//        }).retry(10)
//                .onErrorComplete()
//                .doOnError(new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Throwable {
//                        Log.d("====>", "=============>主线程 ex:");
//                    }
//                })
//                .blockingFirst();

        //  Log.d("====>", "=============>主线程:" + aLong);
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
            "test.ob"
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
        System.out.println("=============>query result:"+find)

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
        System.out.println("=============>query result2:"+find2)
    }
}