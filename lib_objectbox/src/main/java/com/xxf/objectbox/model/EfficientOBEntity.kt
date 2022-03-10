package com.xxf.objectbox.model

/**
 * 高效的模型
 * 1. 避免重复插入
 * 2. 合并
 */
interface EfficientOBEntity {

    fun obInsertable(): Boolean

    fun obMerge(other: Any)
}