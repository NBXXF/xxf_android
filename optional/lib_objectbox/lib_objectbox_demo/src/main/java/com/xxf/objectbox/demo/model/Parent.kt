package com.xxf.objectbox.demo.model

import io.objectbox.annotation.BaseEntity

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2021/11/18
 * Description ://TODO
 */

@BaseEntity
open class Parent {
    var type = "xx"
    override fun toString(): String {
        return "Parent(type='$type')"
    }

}