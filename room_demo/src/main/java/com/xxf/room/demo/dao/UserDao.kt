package com.xxf.room.demo.dao

import androidx.room.*
import com.xxf.room.demo.model.User
import io.reactivex.rxjava3.core.Observable


/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：7/14/21
 * Description ://TODO
 */
@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun loadAll(): List<User>

    @Query("SELECT * FROM user")
    fun loadRxAll(): Observable<List<User>>

    @Query("SELECT * FROM user WHERE id IN (:ids)")
    fun loadAllById(vararg ids: Int): List<User>

    @Query("SELECT * FROM user WHERE name LIKE '%' || (:name) || '%' ")
    fun loadByName(name:String): List<User>

    @Query("SELECT * FROM user WHERE name MATCH (:name)")
    fun loadByName2(name:String): List<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg users: User);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll2(users: List<User>);

    @Delete
    fun delete(song: User);

    /**
     * 全部删除
     */
    @Query("DELETE FROM user")
    fun deleteAllUser()

}