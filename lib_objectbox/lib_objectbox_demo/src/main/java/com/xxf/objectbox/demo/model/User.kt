package com.xxf.objectbox.demo.model

import com.xxf.objectbox.UniqueIndexMergePo
import io.objectbox.Box
import io.objectbox.Property
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Unique
import io.objectbox.kotlin.equal
import io.objectbox.kotlin.inValues
import io.objectbox.query.QueryBuilder

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：7/10/21
 * Description ://用户模型
 */
@Entity
class User(
        // @Id(assignable = true)
        @Id
        var _id: Long,
        @Unique
        var name: String, var age: Int) : UniqueIndexMergePo<User> {


    override fun toString(): String {
        return "User(id=$_id, name='$name', age=$age)"
    }

    override fun setMergeId(id: Long) {
      _id=id;
    }

    override fun getMergeUniqueIndex(): Pair<Property<User>, String> {
        return Pair(User_.name,name);
    }
}

