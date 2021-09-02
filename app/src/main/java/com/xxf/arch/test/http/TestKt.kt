package com.xxf.arch.test.http

import com.xxf.arch.apiService

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2021/9/2
 * Description ://TODO
 */
object TestKt {
    fun test() {
        LoginApiService::class.java.apiService().getCity()
            .subscribe {
                System.out.println("=========>test SY:" + it);
            }
    }
}