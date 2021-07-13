package com.xxf.objectbox

import io.objectbox.Box
import io.objectbox.EntityInfo
import io.objectbox.Property

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：7/13/21
 * Description :// 唯一索引 作为主键来更新和插入
 */
interface UniqueIndexMergePo<T> {
    /**
     * 设置合并主键
     */
    fun setMergeId(id: Long);
    /**
     *  获取合并唯一主键依据
     */
    fun getMergeUniqueIndex():Pair<Property<T>,String>;
}