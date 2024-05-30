package com.xxf.objectbox.exception

import io.objectbox.exception.DbException

/**
 * 数据库线程异常
 */
class DbThreadException:DbException {
    constructor(message: String?) : super(message)
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    constructor(message: String?, errorCode: Int) : super(message, errorCode)
}