package com.xxf.application.clicks

import android.os.SystemClock
/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.1
 * @Description  连续点击事件捕捉 类似打开android 开发者模式
 * @date createTime：2018/9/7
 */
open class QuickClicksHandler(val count: Int = 5, // 点击次数
                              val duration: Long = 1000 // 规定有效时间 毫秒
                            ) {
    private var mHits: LongArray = LongArray(count)
    fun handle(click:()->Unit){
        //每次点击时，数组向前移动一位
        System.arraycopy(mHits, 1, mHits, 0, mHits.size - 1)
        //为数组最后一位赋值
        mHits[mHits.size-1] = SystemClock.uptimeMillis()
        if (mHits[0] >= SystemClock.uptimeMillis() - duration) {
            mHits = LongArray(count) //重新初始化数组
            click();
        }
    }
}