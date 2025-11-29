package com.xxf.qt.english.models

import com.nbxxf.kpower.database.objectbox.entity.BaseEntity
import com.xxf.objectbox.toObjectBoxId
import java.io.Serializable
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
class WordInfo:BaseEntity, Serializable {
    @Id(assignable = true)
    override var id: Long = 0
        get() = word.orEmpty().toObjectBoxId()
    var word:String? = null
    var description:String? = null

    constructor(){

    }
    constructor(word: String?=null, description: String?=null) {
        this.word = word
        this.description = description
    }
}