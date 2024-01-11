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

/**
 * 延迟初始化的委托,能用var修饰,可空修饰
 * 解决kotlin by lazy自带无法修改其值,只能用val修饰的缺陷
 */
fun <V : Any> Delegates.lazy(initializer: () -> V): ReadWriteProperty<Any?, V> =
    SafeLazyReadWriteProperty<V>(initializer)


/**
 * 延迟初始化的委托,能用var修饰,可空修饰
 * 解决kotlin by lazy自带无法修改其值,只能用val修饰的缺陷
 */
fun <V : Any> Delegates.lazy(
    mode: LazyThreadSafetyMode, initializer: () -> V
): ReadWriteProperty<Any?, V> = when (mode) {
    LazyThreadSafetyMode.SYNCHRONIZED -> SafeLazyReadWriteProperty(initializer)
    LazyThreadSafetyMode.PUBLICATION -> SafeLazyReadWriteProperty(initializer)
    LazyThreadSafetyMode.NONE -> LazyReadWriteProperty(initializer)
}

/**
 * 延迟初始化的委托,能用var修饰,可空修饰
 * 解决kotlin by lazy自带无法修改其值,只能用val修饰的缺陷
 *  线程不安全
 */
fun <V : Any> Delegates.lazyUnSafe(
    initializer: () -> V
): ReadWriteProperty<Any?, V> = LazyReadWriteProperty(initializer)


/**
 * 延迟初始化的委托,能用var修饰,可空修饰
 * 解决kotlin by lazy自带无法修改其值,只能用val修饰的缺陷
 */
fun <V : Any> doLazy(initializer: () -> V): ReadWriteProperty<Any?, V> = Delegates.lazy(initializer)

/**
 * 延迟初始化的委托,能用var修饰,可空修饰
 * 解决kotlin by lazy自带无法修改其值,只能用val修饰的缺陷
 */
fun <V : Any> doLazy(
    mode: LazyThreadSafetyMode, initializer: () -> V
): ReadWriteProperty<Any?, V> = Delegates.lazy(mode, initializer)

/**
 * 延迟初始化的委托,能用var修饰,可空修饰
 * 解决kotlin by lazy自带无法修改其值,只能用val修饰的缺陷
 * 线程不安全
 */
fun <V : Any> doLazyUnsafe(
    initializer: () -> V
): ReadWriteProperty<Any?, V> = LazyReadWriteProperty(initializer)

/**
 * 延迟初始化的委托,能用var修饰,可空修饰
 * 解决kotlin by lazy自带无法修改其值,只能用val修饰的缺陷
 * 线程不安全
 */
fun <V : Any> lazyUnsafe(
    initializer: () -> V
): ReadWriteProperty<Any?, V> = doLazyUnsafe(initializer)


private class LazyReadWriteProperty<T : Any>(open val initializer: () -> T) :
    ReadWriteProperty<Any?, T> {
    internal lateinit var value: T
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return if (::value.isInitialized) {
            value
        } else {
            initializer().also {
                this.value = it
            }
        }
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = value
    }
}

private class SafeLazyReadWriteProperty<T : Any>(val initializer: () -> T) :
    ReadWriteProperty<Any?, T> {
    internal lateinit var value: T
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return if (::value.isInitialized) {
            value
        } else {
            synchronized(this) {
                if (::value.isInitialized) {
                    value
                } else {
                    initializer().also {
                        this.value = it
                    }
                }
            }
        }
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = value
    }
}

/**
 * 标准定义KeyValue 的代理
 * 实现可以 intent bundle,prefs
 */
abstract class KeyValueDelegate<in T, V>(open val key: String?, open val default: V) :
    ReadWriteProperty<T, V> {
}

class ObservableDelegate<in T, V>(
    val delegate: ReadWriteProperty<T, V>,
    val beforeChange: (property: KProperty<*>, newValue: V) -> Unit = { _, _ -> },
    val afterChange: (property: KProperty<*>, newValue: V) -> Unit
) : ReadWriteProperty<T, V> {
    override fun getValue(thisRef: T, property: KProperty<*>): V {
        return delegate.getValue(thisRef, property)
    }

    override fun setValue(thisRef: T, property: KProperty<*>, value: V) {
        beforeChange(property, value)
        delegate.setValue(thisRef, property, value)
        afterChange(property, value)
    }
}

/**
 * 观察原始的delegate
 */
fun <T, V> ReadWriteProperty<T, V>.observable(
    beforeChange: (property: KProperty<*>, newValue: V) -> Unit = { _, _ -> },
    afterChange: (property: KProperty<*>, newValue: V) -> Unit
) = ObservableDelegate<T, V>(this, beforeChange, afterChange)