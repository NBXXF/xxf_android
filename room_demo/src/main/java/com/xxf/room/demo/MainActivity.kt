package com.xxf.room.demo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.xxf.room.demo.dao.UserDao
import com.xxf.room.demo.database.UserDatabase
import com.xxf.room.demo.model.User


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        test()
    }


    private fun test()
    {
        val db: UserDatabase = Room
                .databaseBuilder(applicationContext, UserDatabase ::class.java, "test_db4")
                .allowMainThreadQueries()
                .build()

        db.userDao().loadAll().forEach {
            db.userDao().delete(it);
        }


        val user = User();
        user.id=1002;
        user.name="2张三";
        user.releaseYear=1970;
        db.userDao().insertAll(user);


        val user2 = User();
        user2.id=1001;
        user2.name="1李四";
        user2.releaseYear=1970;
        db.userDao().insertAll(user2);

        val loadAll = db.userDao().loadAll();
        Log.d("=====>","all:"+loadAll);

        val start=System.currentTimeMillis();
        db.userDao().insertAll(User(1004,"测试",1),
                User(1005,"测试",1),
                User(1006,"测试",1),
             User(1007,"测试",1));
        val end=System.currentTimeMillis();
        Log.d("=====>","take:"+(end-start));
        yc(db.userDao());
    }

    private fun yc(dao:UserDao)
    {
        Thread(Runnable {
            val list:MutableList<User> = mutableListOf<User>();
            for (index in 0..10000)
            {
                list.add(User(index+9999,"测试:"+index,1))
            }
            val start=System.currentTimeMillis();
            dao.insertAll2(list);
            val end=System.currentTimeMillis();
            Log.d("=====>","take2:"+(end-start));
        }).start();
    }
}