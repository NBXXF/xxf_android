package com.xxf.objectbox.demo

import android.app.Application
import android.content.Context
import com.xxf.objectbox.ObjectBoxFactory
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
        return ObjectBoxFactory.getBoxStore(context.applicationContext!! as Application, MyObjectBox.builder(), User::class.simpleName)!!.boxFor(Teacher::class.java);
    }


    fun add(context: Context,teachers:List<Teacher>) {
        getBox(context).put(teachers);
    }

    fun queryAll(context: Context):List<Teacher> {
       return getBox(context).all;
    }
}