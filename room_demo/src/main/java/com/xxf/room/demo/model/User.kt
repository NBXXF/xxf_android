package com.xxf.room.demo.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey




/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：7/14/21
 * Description :
 */
@Entity
class User {
  //  @PrimaryKey(autoGenerate = true)
    @PrimaryKey
     var id = 0L
     var name: String? = null;
     @ColumnInfo(name = "release_year")
     var releaseYear:Int=0;

    constructor(id: Long, name: String?, releaseYear: Int) {
        this.id = id
        this.name = name
        this.releaseYear = releaseYear
    }

    constructor()

    override fun toString(): String {
        return "User(id=$id, name=$name, releaseYear=$releaseYear)"
    }
}