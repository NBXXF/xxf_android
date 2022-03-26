package com.xxf.objectbox.demo.model

import com.xxf.objectbox.toObjectBoxId
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：7/15/21
 * Description ://TODO
 */
@Entity
class Teacher(
    @Id(assignable = true)
    var id: Long,
    var name: String,
    var age:Int=10
) {
    override fun toString(): String {
        val toObjectBoxId = name.toObjectBoxId();
        return "Teacher(id=$id, name='$name')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Teacher) return false

        if (id != other.id) return false
//        if (name != other.name) return false
//        if (age != other.age) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + age
        return result
    }

}