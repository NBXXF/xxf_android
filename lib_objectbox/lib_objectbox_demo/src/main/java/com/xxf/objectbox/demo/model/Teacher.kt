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
    var id: Long, var name: String
) {
    override fun toString(): String {
        val toObjectBoxId = name.toObjectBoxId();
        return "Teacher(id=$id, name='$name')"
    }
}