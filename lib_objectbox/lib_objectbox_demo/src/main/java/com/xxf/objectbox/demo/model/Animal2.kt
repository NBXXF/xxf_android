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

    var parent:Parent?=null;


    override fun toString(): String {
        return "Animal2(id=$id, uuid=$uuid, name=$name)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Animal2

        if (uuid != other.uuid) return false
        if (name != other.name) return false
        if (parent != other.parent) return false

        return true
    }

    override fun hashCode(): Int {
        var result = uuid?.hashCode() ?: 0
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (parent?.hashCode() ?: 0)
        return result
    }


}