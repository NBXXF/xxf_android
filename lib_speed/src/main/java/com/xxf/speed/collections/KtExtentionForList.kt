package com.xxf.speed.collections

/**
 * 解决java kotlin map默认16个元素的初始化问题
 * 解决java kotlin list默认10个元素的初始化问题
 * 1. 大多数情况,是查询 避免扩容带来的时间消耗,可以很快的且正确设置初始容量
 * 2. 更新扩容能指定扩容因子 能跟随业务的特性 避免频繁扩容带来的时间消耗
 * 3. 我的观点是追求速度的体验 远远大于这点内存的开销(目前android 手机内存足够)
 **/


/**
 * 创建期望大小的List
 * @param expectedSize 初始化大小
 * @return ArrayList
 */
fun <T> mutableListOfExpectedSize(expectedSize: Int): MutableList<T> = ArrayList(expectedSize)

/**
 * 创建期望大小的List
 * @param expectedSize 初始化大小
 * @return ArrayList
 */
fun <T> arrayListOfExpectedSize(expectedSize: Int): ArrayList<T> = ArrayList(expectedSize)


/**
 * 智能判断不会是不是对应的类型
 * toMutableList 本身会创建一个新的
 */
fun <T> List<T>.toMutableListOrCast(): MutableList<T> {
    if (this is MutableList) {
        return this
    }
    return this.toMutableList()
}

/**
 * 智能判断不会是不是对应的类型
 */
fun <T> MutableList<T>.toListOrCast(): List<T> {
    return this
}

