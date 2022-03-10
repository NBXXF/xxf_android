package com.xxf.objectbox.demo.model

import com.xxf.hash.toMurmurHash
import io.objectbox.annotation.ConflictStrategy
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Unique

@Entity
class ContainsListModel {
    @Id(assignable = true)
    var id: Long = 0
        get() = uuid?.toMurmurHash() ?: 0L;

    @Unique(onConflict = ConflictStrategy.REPLACE)
    var uuid: String? = null

    var list: MutableList<String>? = null

    var list2: List<String>? = null
    override fun toString(): String {
        return "ContainsListModel(uuid=$uuid, list=$list, list2=$list2)"
    }


}