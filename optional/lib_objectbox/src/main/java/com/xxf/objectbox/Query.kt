package com.xxf.objectbox

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2018/7/16 17:34
 * Description :
 */
import com.xxf.objectbox.model.PageInfoDTO
import io.objectbox.query.Query
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

/**
 * Shortcut for [`RxQuery.flowableOneByOne(query, strategy)`][RxQuery.flowableOneByOne].
 */
fun <T : Any> Query<T>.flowableOneByOne(strategy: BackpressureStrategy = BackpressureStrategy.BUFFER): Flowable<T> {
    return RxQuery.flowableOneByOne(this, strategy)
}

/**
 * Shortcut for [`RxQuery.observable(query)`][RxQuery.observable].
 */
fun <T> Query<T>.observable(): Observable<List<T>> {
    return RxQuery.observable(this)
}

/**
 * Shortcut for [`RxQuery.observable(query)`][RxQuery.observable].
 */
fun <T> Query<T>.observableChange(): Observable<List<T>> {
    return RxQuery.observableChange(this)
}

/**
 * Shortcut for [`RxQuery.single(query)`][RxQuery.single].
 */
fun <T> Query<T>.single(): Single<List<T>> {
    return RxQuery.single(this)
}

/**
 * 是否有结果 这种查询速度最快
 * 比count快
 * 比findFist快
 */
fun <T> Query<T>.hasResult(): Boolean {
    return this.findIds(0, 1).isNotEmpty();
}

/**
 * 分页查询
 * @param pageNum 从1开始
 * @param pageSize 大于0
 */
fun <T> Query<T>.findPage(pageNum: Long, pageSize: Long): PageInfoDTO<T> {
    val count = this.count()
    return this.find((pageNum - 1) * pageSize, pageSize).run {
        PageInfoDTO<T>(
            pageNum = pageNum,
            pageSize = pageSize,
            hasNextPage = count > pageSize * pageNum,
            total = count,
            list = this
        )
    }
}