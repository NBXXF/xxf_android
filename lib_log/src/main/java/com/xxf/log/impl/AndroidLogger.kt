package com.xxf.log.impl

import android.util.Log
import com.xxf.log.Logger

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.1
 * @Description  android 默认实现
 * @date createTime：2018/9/7
 */
class AndroidLogger:Logger {
    override fun logV(tag: String?, log: () -> Any) {
        Log.v(tag?:"","${log()}")
    }

    override fun logI(tag: String?, log: () -> Any) {
        Log.i(tag?:"","${log()}")
    }

    override fun logD(tag: String?, log: () -> Any) {
        Log.d(tag?:"","${log()}")
    }

    override fun logE(tag: String?, log: () -> Any) {
        Log.e(tag?:"","${log()}")
    }

    override fun logW(tag: String?, log: () -> Any) {
        Log.w(tag?:"","${log()}")
    }

    override fun logJson(tag: String?, log: () -> Any) {
        Log.i(tag?:"","${log()}")
    }

}