package com.xxf.objectbox.demo.nested

import com.xxf.hash.toMurmurHash
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.converter.FlexObjectConverter

@Entity
class TestPO {
    @Id(assignable = true)
    var id: Long = 0
        get() = uuid?.toMurmurHash() ?: 0L;

    var uuid: String? = null

    /**
     * 测试嵌套
     */
    @Convert(converter = FlexObjectConverter::class, dbType = ByteArray::class)
    var nest: Any? = null
    override fun toString(): String {
        return "TestPO(uuid=$uuid, nest=$nest)"
    }
}


class TestChildPO {
    var name: String? = null
    override fun toString(): String {
        return "TestChildPO(name=$name)"
    }
}