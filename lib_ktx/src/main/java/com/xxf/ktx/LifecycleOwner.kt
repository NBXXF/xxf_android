package com.xxf.ktx

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.1
 * @Description  LifecycleOwner 相关扩展
 * @date createTime：2018/9/7
 */

/**
 * 获取 容器 activity
 * 如果是fragment 那么获取的是fragment.activity
 */
fun LifecycleOwner.findActivity():Activity{
   return this.asFragmentOrNull()?.requireActivity()?: this.asActivityOrNull()!!
}

/**
 * 转换成activity
 */
fun LifecycleOwner.asActivityOrNull():Activity?{
    return this as? Activity
}

/**
 * 转换成FragmentActivity
 */
fun LifecycleOwner.asFragmentActivityOrNull():FragmentActivity?{
    return this as? FragmentActivity
}
/**
 * 转换fragment
 */
fun LifecycleOwner.asFragmentOrNull():Fragment?{
    return this as? Fragment
}