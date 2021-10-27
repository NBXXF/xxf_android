package com.xxf.objectbox.demo.model

import com.xxf.hash.toMurmurHash
import io.objectbox.annotation.*

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2021/10/26
 * Description ://测试唯一索引作为主键
 */
@Entity
class Animal2 {
    @Id(assignable = true)
    var id: Long = 0
        get() = uuid?.toMurmurHash() ?: 0L;

    @Unique(onConflict = ConflictStrategy.REPLACE)
   // @Index(type = IndexType.HASH64)
    var uuid: String? = null

    var name: String? = null


    override fun toString(): String {
        return "Animal2(id=$id, uuid=$uuid, name=$name)"
    }

}