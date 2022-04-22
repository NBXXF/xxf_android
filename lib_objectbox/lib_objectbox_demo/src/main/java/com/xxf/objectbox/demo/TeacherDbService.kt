package com.xxf.objectbox.demo

import android.content.Context
import com.xxf.objectbox.buildSingle
import com.xxf.objectbox.demo.model.MyObjectBox
import com.xxf.objectbox.demo.model.Teacher
import com.xxf.objectbox.demo.model.User
import io.objectbox.Box

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：7/15/21
 * Description ://TODO
 */
object TeacherDbService {
    private fun getBox(context: Context): Box<Teacher> {
        return MyObjectBox.builder()
            .buildSingle(User::class.simpleName!!,allowMainThreadOperation = true)
            .boxFor(Teacher::class.java);
    }


    fun add(context: Context, teachers: List<Teacher>) {
        getBox(context).put(teachers);
    }

    fun queryAll(context: Context): List<Teacher> {
        return getBox(context).all;
    }
}