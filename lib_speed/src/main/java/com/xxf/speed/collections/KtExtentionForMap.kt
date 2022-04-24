package com.xxf.speed.collections

/**
 * 解决java kotlin map默认16个元素的初始化问题
 * 解决java kotlin list默认10个元素的初始化问题
 * 1. 大多数情况,是查询 避免扩容带来的时间消耗,可以很快的且正确设置初始容量
 * 2. 更新扩容能指定扩容因子 能跟随业务的特性 避免频繁扩容带来的时间消耗
 * 3. 我的观点是追求速度的体验 远远大于这点内存的开销(目前android 手机内存足够)
 **/

/**
 * 创建期望大小的map
 * @param expectedSize 初始化大小
 * @return linkedHashMap
 */
fun <K, V> mutableMapOfExpectedSize(expectedSize: Int): MutableMap<K, V> =
    LinkedHashMap(mapCapacity(expectedSize))

/**
 * 创建期望大小的map
 * @param expectedSize 初始化大小
 * @return linkedHashMap
 */
fun <K, V> hashMapOfExpectedSize(expectedSize: Int): HashMap<K, V> =
    HashMap<K, V>(mapCapacity(expectedSize))

/**
 * 创建期望大小的 LongHashMap
 * @param expectedSize 初始化大小
 * @return linkedHashMap
 */
fun <V> longMapOfExpectedSize(expectedSize: Int) = LongHashMap<V>(mapCapacity(expectedSize))

/**
 * 创建期望大小的map
 * @param expectedSize 初始化大小
 * @return linkedHashMap
 */
fun <K, V> linkedMapOfExpectedSize(expectedSize: Int): LinkedHashMap<K, V> =
    LinkedHashMap<K, V>(mapCapacity(expectedSize))


fun mapCapacity(expectedSize: Int): Int = when {
    // We are not coercing the value to a valid one and not throwing an exception. It is up to the caller to
    // properly handle negative values.
    expectedSize < 0 -> expectedSize
    expectedSize < 3 -> expectedSize + 1
    expectedSize < INT_MAX_POWER_OF_TWO -> ((expectedSize / 0.75F) + 1.0F).toInt()
    // any large value
    else -> Int.MAX_VALUE
}

private const val INT_MAX_POWER_OF_TWO: Int = 1 shl (Int.SIZE_BITS - 2)
