package com.xxf.objectbox.demo

import android.app.Application
import android.content.Context
import android.util.Log
import com.xxf.objectbox.*
import com.xxf.objectbox.demo.model.MyObjectBox
import com.xxf.objectbox.demo.model.User
import com.xxf.objectbox.demo.model.User_
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

    class MergeClass:DbMergeBlock<User>
    {
        override fun invoke(insertData: List<User>, box: Box<User>): List<User> {
            Log.d("=====>","DbMergeBlock:"+insertData);
            return insertData;
        }
    }

    fun replaceTable(context: Context, users: List<User>) {
        //方式1
//        getBox(context).replaceTable(users,object :MergeBlock<User>{
//            override fun invoke(insertData: List<User>, box: Box<User>): List<User> {
//                  return  insertData;
//            }
//        });

        //方式2
//        getBox(context).replaceTable(users,{
//            insertData: List<User>, box: Box<User> ->
//            insertData;
//        });

        //方式3
        getBox(context).replaceTable(users,MergeClass());
    }

    fun add(context: Context, user: User): Long {
        return getBox(context).put(user);
    }

    fun addAll(context: Context, users: List<User>) {
        val start=System.currentTimeMillis();
       // getBox(context).put(users,UniqueIndexMergeBlock());
        getBox(context).put(users);
        val end=System.currentTimeMillis();
        Log.d("======>","take:"+(end-start));
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

    fun clearTable(context: Context)
    {
        getBox(context).removeAll();
    }

    //按条件查询
    fun queryById(context: Context, id: Long): User? {
        return getBox(context).query()
               // .equal(User_.id, id)
                .build()
                .findFirst();
    }
}

