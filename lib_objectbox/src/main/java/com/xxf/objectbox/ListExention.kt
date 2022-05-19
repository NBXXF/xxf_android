package com.xxf.objectbox

/**
 * 将查找出来的数据 转换成有序map
 * 尤其是在sql in 场景
 * @param keySortList 指定key的顺序
 */
fun <T> List<T>.toKeySortMap(
    keySortList: Array<String>,
    keySelector: (T) -> String
): LinkedHashMap<String, T> {
    //无序的
    val results = this
    val resultsMap = results.associateBy(keySelector)
    val sortMap = LinkedHashMap<String, T>(results.size)
    keySortList.forEach { key ->
        resultsMap.get(key)?.let {
            sortMap.put(key, it)
        }
    }
    return sortMap
}

/**
 * 将查找出来的数据 转换成有序List
 *  * 尤其是在sql in [2,4,5,6] 场景
 *   @param keySortList 指定key的顺序
 */
fun <T> List<T>.toKeySortList(
    keySortList: Array<String>,
    keySelector: (T) -> String
): ArrayList<T> {
    //无序的
    val results = this
    val resultsMap = results.associateBy(keySelector)
    return keySortList.mapNotNull {
        resultsMap.get(it)
    } as ArrayList<T>
}

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
    mapOf<String, String>().toMutableMap()
    return this
}

/**
 * 智能判断不会是不是对应的类型
 */
fun <K, V> Map<K, V>.toMutableMapOrCast(): MutableMap<K, V> {
    if (this is MutableMap) {
        return this
    }
    return this.toMutableMap()
}

/**
 * 智能判断不会是不是对应的类型
 */
fun <K, V> MutableMap<K, V>.toMapOrCast(): Map<K, V> {
    return this
}

/**
 * 智能判断不会是不是对应的类型
 */
fun <T> Set<T>.toMutableSetOrCast(): MutableSet<T> {
    if (this is MutableSet) {
        return this
    }
    return this.toMutableSet()
}

/**
 * 智能判断不会是不是对应的类型
 */
fun <T> MutableSet<T>.toSetOrCast(): Set<T> {
    return this
}
