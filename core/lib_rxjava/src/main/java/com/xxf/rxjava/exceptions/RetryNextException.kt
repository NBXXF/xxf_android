package com.xxf.rxjava.exceptions

import java.io.IOException

/**
 * 重试下一步异常,比如下一页  定义rx流摸版异常
 * 下一步 下一页等等
 */
open class RetryNextException : IOException {
    constructor() : super()
    constructor(message: String?) : super(message)
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    constructor(cause: Throwable?) : super(cause)
}