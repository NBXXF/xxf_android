package com.xxf.ktx.standard

import kotlin.properties.Delegates
import kotlin.properties.PropertyDelegateProvider
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * 提供回调式 只读委托
 */
fun <V> Delegates.readOnly(read: (thisRef: Any?, property: KProperty<*>) -> V) =
    ReadOnlyProperty<Any?, V> { thisRef, property -> read(thisRef, property) }

/**
 * 提供回调式 可读可写委托
 */
fun <V> Delegates.readWrite(
    read: (thisRef: Any?, property: KProperty<*>) -> V,
    write: (thisRef: Any?, property: KProperty<*>, value: V) -> Unit
) = object : ReadWriteProperty<Any?, V> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): V {
        return read(thisRef, property)
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: V) {
        write(thisRef, property, value)
    }
}

/**
 * 提供回调式 委托提供者
 */
fun <D> Delegates.propertyDelegateProvider(provide: (thisRef: Any?, property: KProperty<*>) -> D) =
    PropertyDelegateProvider<Any?, D> { thisRef, property ->
        provide(thisRef, property)
    }
