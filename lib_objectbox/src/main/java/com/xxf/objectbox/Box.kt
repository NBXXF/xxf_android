package com.xxf.objectbox

import io.objectbox.Box

/**
 * Get the stored object for the given ID.
 *
 * @return null if not found
 */
fun <T> Box<T>.getSafe(id: Long): T? {
    return try {
        this.get(id)
    }  catch (e:IllegalStateException){
        this.closeThreadResources()
        this.get(id)
    }
}

/**
 * Get the stored objects for the given IDs.
 *
 * @return null if not found
 */
fun <T> Box<T>.getSafe(ids: Iterable<Long?>?): MutableList<T> {
    this.closeThreadResources()
    return try {
        this.get(ids)
    }  catch (e:IllegalStateException){
        this.closeThreadResources()
        this.get(ids)
    }
}

/**
 * Get the stored objects for the given IDs.
 *
 * @return null if not found
 */
fun <T> Box<T>.getSafe(ids: LongArray): MutableList<T> {
    return try {
        this.get(ids)
    }  catch (e:IllegalStateException){
        this.closeThreadResources()
        this.get(ids)
    }
}

/**
 * Get the stored objects for the given IDs as a Map with IDs as keys, and entities as values.
 * IDs for which no entity is found will be put in the map with null values.
 *
 * @return null if not found
 */
fun <T> Box<T>.getMapSafe(ids: Iterable<Long>?): MutableMap<Long, T> {
    return try {
        this.getMap(ids)
    } catch (e:IllegalStateException){
        this.closeThreadResources()
        this.getMap(ids)
    }
}
/**
 * Returns the count of all stored objects in this box or the given maxCount, whichever is lower.
 *
 * @param maxCount maximum value to count or 0 (zero) to have no maximum limit
 */
/**
 * Returns the count of all stored objects in this box.
 */

fun <T> Box<T>.countSafe(maxCount: Long = 0): Long {
    return try {
        this.count(maxCount)
    }catch (e:IllegalStateException){
        this.closeThreadResources()
        this.count(maxCount)
    }
}

/** Returns true if no objects are in this box.  */
fun <T> Box<T>.isEmptySafe(): Boolean {
    return this.countSafe(1) == 0L
}

/**
 * Returns all stored Objects in this Box.
 * @return since 2.4 the returned list is always mutable (before an empty result list was immutable)
 */
fun <T> Box<T>.getAllSafe(): List<T> {
    return try {
        this.getAll()
    } catch (e:IllegalStateException){
        this.closeThreadResources()
        this.getAll()
    }
}

/**
 * Check if an object with the given ID exists in the database.
 * This is more efficient than a [.get] and comparing against null.
 * @since 2.7
 * @return true if a object with the given ID was found, false otherwise
 */
fun <T> Box<T>.containsSafe(id: Long): Boolean {
    return try {
        this.contains(id)
    }  catch (e:IllegalStateException){
        this.closeThreadResources()
        this.contains(id)
    }
}
