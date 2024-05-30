package com.xxf.objectbox.demo.nested

import com.xxf.objectbox.buildSingle
import com.xxf.objectbox.demo.MyObjectBox
import io.objectbox.Box
import java.util.Date
import java.util.UUID

class TestPODbService {
    private fun getBox(): Box<TestPO> {
        return MyObjectBox.builder()
            .buildSingle(TestPO::class.simpleName!!,allowMainThreadOperation = true)
            .boxFor(TestPO::class.java);
    }

    fun testAdd(){
        getBox().put(TestPO().apply {
            this.uuid= UUID.randomUUID().toString()
            this.nest= TestChildPO().apply {
                this.name= Date().toString()
            }
        })
    }

    fun testQuery(){
        val all = getBox().all;
        println("===================>all:$all")
    }

    fun add(teachers: List<TestPO>) {
        getBox().put(teachers);
    }

    fun queryAll(): List<TestPO> {
        return getBox().all;
    }
}