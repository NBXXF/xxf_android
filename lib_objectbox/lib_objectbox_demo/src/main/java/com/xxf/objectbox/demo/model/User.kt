package com.xxf.objectbox.demo.model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：7/10/21
 * Description ://用户模型
 */
@Entity
class User(
        @Id(assignable = true)
        var id:Long,var name:String,var age:Int) {
        override fun toString(): String {
                return "User(id=$id, name='$name', age=$age)"
        }
}