package com.xxf.arch.test

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.xxf.application.ApplicationInitializer
import com.xxf.arch.service.*
import com.xxf.arch.service.SpService.getString
import com.xxf.arch.service.SpService.observeAllChange
import com.xxf.arch.service.SpService.observeChange
import com.xxf.arch.service.SpService.putString
import com.xxf.arch.test.databinding.ActivityMainBinding
import com.xxf.rxjava.bindLifecycle
import com.xxf.rxjava.filterWhen
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class SpActivity : AppCompatActivity() {
    class MySpervice : SpServiceDelegate() {
        var id2: String by bindString(key = "xx", defaultValue = "xx")

        var age2: Int by bindInt()

        var bean2: TestBean? by bindObject()

        var finishDiffer: Boolean by bindBoolean(differUser = true)
        var finish: Boolean by bindBoolean(differUser = false)
    }

    class TestBean(val id: String, val name: String) {
        override fun toString(): String {
            return "TestBean(id='$id', name='$name')"
        }
    }

    val binding by viewBinding(ActivityMainBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        mutableListOf<String>()
        Observable.interval(1, TimeUnit.SECONDS)
            .filterWhen(this, Lifecycle.State.RESUMED)
            .doOnDispose {
                System.out.println("================>截流:取消了")
            }
            .doOnError {
                System.out.println("================>截流异常:" + it)
            }
            .doOnComplete {
                System.out.println("================>截流:完成")
            }
            .bindLifecycle(this)
            .subscribe {
                System.out.println("================>截流:" + it)
            }

        System.out.println("================>context:" + ApplicationInitializer.applicationContext)


        //显示一个包含导航控制器的的bottomsheet
//        XXFBottomSheetNavigationDialogFragment { FirstFragment() }
//            .show(supportFragmentManager, "xxx")


        val key = "hello"
        println("===============>all:"+SpService.getAll())
        observeChange(key)
            .subscribe { s -> println("=========>changeKey:" + s + "  v:" + getString(key, "")) }
        observeAllChange()
            .subscribe { s -> println("=========>changeKey2:" + s + "  v:" + getString(key, "")) }


        putString(key, "yes")

        putString(key, "yes")


        val service = MySpervice()
        println("=========>differ_user:"+service.finishDiffer);
        println("=========>differ:"+service.finish);

        service.finishDiffer=true
        service.finish=false
        service.id2 = "hello_" + System.currentTimeMillis()



        service.bean2 = TestBean("" + System.currentTimeMillis(), "张三")

    }

    override fun onResume() {
        super.onResume()
        testRx()
    }

    private fun testRx() {
        Thread(Runnable {
            System.out.println("=================>test start:" + Thread.currentThread().name)
            Observable.interval(1, TimeUnit.SECONDS)
                // Observable.just(100L)
                .doOnNext {
                    System.out.println("=================>test next:" + it + "  thread:" + Thread.currentThread().name)
                }
//                .flatMap {
//                    Observable.just(it)
//                        .subscribeOnIO()
//                }
                .doOnNext {
                    System.out.println("=================>test next map:" + it + "  thread:" + Thread.currentThread().name)
                }
                .doOnError {
                    System.out.println("=================>test throw:" + it + "  thread:" + Thread.currentThread().name)
                }
                .doOnDispose {
                    System.out.println("=================>test dispose:" + "  thread:" + Thread.currentThread().name)
                }
                .subscribeOn(Schedulers.io())
                .bindLifecycle(this, Lifecycle.Event.ON_PAUSE)
                .subscribe {
                    System.out.println("=================>test next end:" + it + "  thread:" + Thread.currentThread().name)
                }
        }).start()
    }
}
