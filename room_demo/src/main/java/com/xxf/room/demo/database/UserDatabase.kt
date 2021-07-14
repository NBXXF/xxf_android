package com.xxf.room.demo.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.xxf.room.demo.dao.UserDao
import com.xxf.room.demo.model.User

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：7/14/21
 * Description ://TODO
 */
@Database(entities = [User::class],version = 1)
abstract class UserDatabase: RoomDatabase() {
  abstract fun userDao():UserDao;
}