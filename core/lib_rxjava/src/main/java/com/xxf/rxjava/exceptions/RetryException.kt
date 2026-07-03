package com.xxf.rxjava.exceptions

import java.io.IOException

/**
 * 重试异常 定义rx流摸版异常
 * 重试当前
 */
open class RetryException : IOException {
    constructor() : super()
    constructor(message: String?) : super(message)
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    constructor(cause: Throwable?) : super(cause)
}