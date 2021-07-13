package com.xxf.objectbox.demo

import android.app.Application
import android.content.Context
import com.xxf.objectbox.ObjectBoxFactory
import com.xxf.objectbox.demo.model.MyObjectBox
import com.xxf.objectbox.demo.model.User
import com.xxf.objectbox.demo.model.User_
import com.xxf.objectbox.replaceTable
import io.objectbox.Box

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：7/10/21
 * Description ://数据库service
 */
object UserDbService {
    private fun getBox(context: Context): Box<User> {
        return ObjectBoxFactory.getBoxStore(context.applicationContext!! as Application, MyObjectBox.builder(), User::class.simpleName)!!.boxFor(User::class.java);
    }


    fun add(context: Context, user: User): Long {
        return getBox(context).put(user);
    }

    fun addAll(context: Context, users: List<User>) {
        getBox(context).put(users)
    }

    fun replaceTable(context: Context, users: List<User>) {
        getBox(context).replaceTable(users) { insertData: List<User>, box: Box<User> ->
            insertData;
        };
    }

    fun update(context: Context, user: User): Long {
        return getBox(context).put(user)
    }

    fun delete(context: Context, user: User): Boolean {
        return getBox(context).remove(user);
    }

    fun queryAll(context: Context): MutableList<User> {
        return getBox(context).all;
    }

    //按条件查询
    fun queryById(context: Context, id: Long): User? {
        return getBox(context).query()
                .equal(User_.id, id)
                .build()
                .findFirst();
    }
}