package com.xxf.arch.test.http

import com.xxf.arch.apiService
import com.xxf.arch.utils.CopyUtils
import com.xxf.utils.d

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2021/9/2
 * Description ://TODO
 */
object TestKt {
    fun test() {
        val copy = CopyUtils.copy(this);
        d("======>hell1334")
        LoginApiService::class.java.apiService().getCity()
            .subscribe {
                d("=========>test SY:" + it);
            }
    }
}