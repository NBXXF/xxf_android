package com.xxf.demo

import android.app.Application
import com.xxf.demo.parser.DefaultParser
import com.xxf.demo.parser.addParser
/**
 * @version 2.3.1
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description
 * @date createTime：2023/11/29
 */
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        addParser(DefaultParser())
    }
}