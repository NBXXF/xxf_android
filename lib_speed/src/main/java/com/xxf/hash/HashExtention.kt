package com.xxf.hash

/**
 * 转换成Murmur hash 默认3A
 */
fun String.toMurmurHash(): Long {
    val primitiveDataChecksum = PrimitiveDataChecksum(Murmur3A());
    primitiveDataChecksum.updateUtf8(this);
    return primitiveDataChecksum.value;
}

/**
 * city hash
 */
fun String.toCityHash(): Long {
    val toByteArray = this.toByteArray()
    return CityHash.cityHash64(toByteArray, 0, toByteArray.size)
}