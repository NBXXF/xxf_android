package com.xxf.objectbox.demo.model

import com.google.gson.Gson
import com.xxf.objectbox.toObjectBoxId
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.converter.PropertyConverter

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

    var age: Int = 10,

    @Convert(converter = MyConvertor::class, dbType = String::class)
    var user:Student
) {

    class Student{
        val name="xxx"
    }
    class MyConvertor : PropertyConverter<Student, String> {
        constructor()
        {
            System.out.println("==============>init PropertyConverter:"+this)
        }

        override fun convertToEntityProperty(databaseValue: String): Student {
            System.out.println("==============>init convertToEntityProperty:"+this)
            try {
                return Gson().fromJson(databaseValue,Student::class.java)
            }catch (e:Throwable)
            {
               return Student()
            }
        }

        override fun convertToDatabaseValue(entityProperty: Student): String {
            System.out.println("==============>init convertToDatabaseValue:"+this)
            return Gson().toJson(entityProperty)
        }

    }

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