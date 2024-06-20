package com.xxf.objectbox.model


/**
 * @Description:
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2024/03/14 17:19
 */
data class PageInfoDTO<T>(
    /**
     * 当前页 从1开始
     */
    var pageNum: Long,

    /**
     * 每页的数量
     */
    var pageSize: Long,

    /**
     * 是否有下一页
     */
    var hasNextPage: Boolean,

    /**
     *可能没有 如果是逻辑分页的情况
     */
    var total: Long?,


    /**
     * 数据
     */
    var list: List<T>
)

/**
 * 转换
 */
fun <T, R> PageInfoDTO<T>.convert(transform: (T) -> R): PageInfoDTO<R> {
    val map = this.list.map(transform)
    return PageInfoDTO(this.pageNum, this.pageSize, this.hasNextPage, this.total, map)
}

fun <T, R> PageInfoDTO<T>.convert(newList: List<R>): PageInfoDTO<R> {
    return PageInfoDTO(this.pageNum, this.pageSize, this.hasNextPage, this.total, newList)
}