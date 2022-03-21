package com.xxf.arch.test

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import com.xxf.application.ApplicationInitializer
import com.xxf.arch.fragment.navigation.container.XXFBottomSheetNavigationDialogFragment
import com.xxf.arch.service.*
import com.xxf.arch.service.SpService.getString
import com.xxf.arch.service.SpService.observeAllChange
import com.xxf.arch.service.SpService.observeChange
import com.xxf.arch.service.SpService.putString
import com.xxf.arch.test.navigationdemo.FirstFragment
import com.xxf.rxjava.bindLifecycle
import com.xxf.rxjava.filterWhen
import com.xxf.utils.d
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit

class SpActivity : AppCompatActivity() {
    class MySpervice : SpServiceDelegate() {
        var id2: String by bindString(key = "xx", defaultValue = "xx")

        var age2: Int by bindInt()

        var bean2: TestBean? by bindObject()

    }

    class TestBean(val id: String, val name: String) {
        override fun toString(): String {
            return "TestBean(id='$id', name='$name')"
        }
    }

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
        XXFBottomSheetNavigationDialogFragment { FirstFragment() }
            .show(supportFragmentManager, "xxx")


        val key = "hello"
        d(key, null, "================")
        d(key, null, "================22222")
        observeChange(key)
            .subscribe { s -> println("=========>changeKey:" + s + "  v:" + getString(key, "")) }
        observeAllChange()
            .subscribe { s -> println("=========>changeKey2:" + s + "  v:" + getString(key, "")) }

        putString(key, "yes")

        putString(key, "yes")


        val service = MySpervice()
        d("==========>sp id:" + service.id2)
        service.id2 = "hello_" + System.currentTimeMillis()
        d("==========>sp  saved id:" + service.id2)


        d("==========>sp bean:" + service.bean2)
        service.bean2 = TestBean("" + System.currentTimeMillis(), "张三")
        d("==========>sp  saved bean:" + service.bean2)
    }
}
