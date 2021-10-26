package com.xxf.objectbox.demo.model

import io.objectbox.annotation.ConflictStrategy
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Unique

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2021/10/26
 * Description ://测试唯一索引作为主键
 */
@Entity
class Animal {
    @Id(assignable = true)
    var id: Long = 0;

    @Unique(onConflict = ConflictStrategy.REPLACE)
    var uuid: String? = null

    var name: String? = null


    override fun toString(): String {
        return "Animal(id=$id, uuid=$uuid, name=$name)"
    }

}