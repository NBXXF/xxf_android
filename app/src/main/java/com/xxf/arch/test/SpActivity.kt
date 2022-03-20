package com.xxf.arch.test

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.xxf.application.ApplicationInitializer
import com.xxf.arch.fragment.navigation.container.XXFBottomSheetNavigationDialogFragment
import com.xxf.arch.service.*
import com.xxf.arch.service.SpService.getString
import com.xxf.arch.service.SpService.observeAllChange
import com.xxf.arch.service.SpService.observeChange
import com.xxf.arch.service.SpService.putString
import com.xxf.arch.test.navigationdemo.FirstFragment


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

        System.out.println("================>context:"+ApplicationInitializer.applicationContext)


        //显示一个包含导航控制器的的bottomsheet
        XXFBottomSheetNavigationDialogFragment { FirstFragment() }
            .show(supportFragmentManager, "xxx")


        val key = "hello"
        observeChange(key)
            .subscribe { s -> println("=========>changeKey:" + s + "  v:" + getString(key, "")) }
        observeAllChange()
            .subscribe { s -> println("=========>changeKey2:" + s + "  v:" + getString(key, "")) }

        putString(key, "yes")

        putString(key, "yes")


        val service = MySpervice()
        service.id2 = "hello_" + System.currentTimeMillis()



        service.bean2 = TestBean("" + System.currentTimeMillis(), "张三")

    }
}
