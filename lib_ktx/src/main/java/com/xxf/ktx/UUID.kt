package com.xxf.ktx

import java.util.UUID

/**
 * 原始36位
 */
@Deprecated(
    "命名不标准",
    replaceWith = ReplaceWith("请用randomUUIDString36 或者randomUUIDString32")
)
inline val randomUUIDString: String
    get() = UUID.randomUUID().toString()

/**
 * 原始36位
 */
inline val randomUUIDString36: String
    get() = UUID.randomUUID().toString()

/**
 * 去除中横线 32位
 */
inline val randomUUIDString32: String
    get() = UUID.randomUUID().string32()

/**
 * 去除中横线 32位
 */
fun UUID.string32(): String {
    return toString().replace("-", "")
}