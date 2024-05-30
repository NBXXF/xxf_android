package com.xxf.objectbox.demo

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.xxf.objectbox.buildSingle
import com.xxf.objectbox.demo.nested.TestChildPO
import com.xxf.objectbox.demo.nested.TestPO
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Date
import java.util.UUID

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        Assert.assertEquals("com.xxf.objectbox.demo", appContext.packageName)
    }

    var box = MyObjectBox.builder()
        .buildSingle(TestPO::class.java.simpleName)
        .boxFor(TestPO::class.java)

    @Test
    fun insert(){
        box.put(TestPO().apply {
            this.uuid=UUID.randomUUID().toString()
            this.nest= TestChildPO().apply {
                this.name=Date().toString()
            }
        })
    }

    fun query(){
        val all = box.all
        println("===================>all:$all")
    }
}