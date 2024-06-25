package com.xxf.objectbox

import io.objectbox.Property
import io.objectbox.query.OrderFlags
import io.objectbox.query.QueryBuilder
import java.util.*


fun <T> QueryBuilder<T>.`in`(
    property: Property<T>,
    values: Array<Boolean>
): QueryBuilder<T> {
    values.forEach {
        this.equal(property, it)
    }
    return this
}

fun <T> QueryBuilder<T>.`in`(
    property: Property<T>,
    values: Array<Date>
): QueryBuilder<T> {
    values.forEach {
        this.equal(property, it)
    }
    return this
}


fun <T> QueryBuilder<T>.notIn(
    property: Property<T>,
    values: Array<String>,
    order: QueryBuilder.StringOrder
): QueryBuilder<T> {
    values.forEach {
        this.notEqual(property, it, order)
    }
    return this
}

fun <T> QueryBuilder<T>.notIn(
    property: Property<T>,
    values: Array<Boolean>
): QueryBuilder<T> {
    values.forEach {
        this.notEqual(property, it)
    }
    return this
}

fun <T> QueryBuilder<T>.notIn(
    property: Property<T>,
    values: Array<Date>
): QueryBuilder<T> {
    values.forEach {
        this.notEqual(property, it)
    }
    return this
}


fun <T> QueryBuilder<T>.order(
    property: Property<T>,
    desc: Boolean
): QueryBuilder<T> {
    return this.order(property, if (desc) OrderFlags.DESCENDING else 0)
}