package com.xxf.activityresult.contracts

import android.content.Context
/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.1
 * @Description  特性功能 约束 是否可用
 * @date createTime：2020/9/3
 */
internal interface EnableContract {
    fun isEnabled(context: Context?): Boolean
}