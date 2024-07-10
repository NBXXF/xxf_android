package com.xxf.drouter

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.SparseArray
import com.didi.drouter.api.Extend.START_ACTIVITY_ANIMATION
import com.didi.drouter.api.Extend.START_ACTIVITY_FLAGS
import com.didi.drouter.router.Request
import com.didi.drouter.router.RouterCallback
import java.io.Serializable


/**
 * BE ATTENTION TO THIS METHOD WAS <P>SET, NOT ADD!</P>
 */
fun Request.with(bundle: Bundle?): Request {
    if (null != bundle) {
        putExtras(bundle)
    }
    return this
}

/**
 * Set special flags controlling how this intent is handled.  Most values
 * here depend on the type of component being executed by the Intent,
 * specifically the FLAG_ACTIVITY_* flags are all for use with
 * [Context.startActivity()][Context.startActivity] and the
 * FLAG_RECEIVER_* flags are all for use with
 * [Context.sendBroadcast()][Context.sendBroadcast].
 */
fun Request.withFlags(flag: Int): Request {
    putExtra(START_ACTIVITY_FLAGS, flag)
    return this
}

/**
 * Add additional flags to the intent (or with existing flags
 * value).
 *
 * @param flags The new flags to set.
 * @return Returns the same Intent object, for chaining multiple calls
 * into a single statement.
 * @see .withFlags
 */
fun Request.addFlags(flags: Int): Request {
    val flag = getFlags() or flags
    putExtra(START_ACTIVITY_FLAGS, flag)
    return this
}

fun Request.getFlags(): Int {
    return getInt(START_ACTIVITY_FLAGS)
}

/**
 * Set object value, the value will be convert to string by 'Fastjson'
 *
 * @param key   a String, or null
 * @param value a Object, or null
 * @return current
 */
fun Request.withObject(
    key: String?,
    value: Any?
): Request {
    when (value) {
        is Serializable -> {
            putExtra(key, value)
        }

        is Parcelable -> {
            putExtra(key, value)
        }

        else -> {
            require(false) {
                "参数类型不匹配"
            }
        }
    }
    return this
}


// Follow api copy from #{Bundle}
/**
 * Inserts a String value into the mapping of this Bundle, replacing
 * any existing value for the given key.  Either key or value may be null.
 *
 * @param key   a String, or null
 * @param value a String, or null
 * @return current
 */
fun Request.withString(
    key: String?,
    value: String?
): Request {
    putExtra(key, value)
    return this
}

/**
 * Inserts a Boolean value into the mapping of this Bundle, replacing
 * any existing value for the given key.  Either key or value may be null.
 *
 * @param key   a String, or null
 * @param value a boolean
 * @return current
 */
fun Request.withBoolean(
    key: String?,
    value: Boolean
): Request {
    putExtra(key, value)
    return this
}

/**
 * Inserts a short value into the mapping of this Bundle, replacing
 * any existing value for the given key.
 *
 * @param key   a String, or null
 * @param value a short
 * @return current
 */
fun Request.withShort(key: String?, value: Short): Request {
    putExtra(key, value)
    return this
}

/**
 * Inserts an int value into the mapping of this Bundle, replacing
 * any existing value for the given key.
 *
 * @param key   a String, or null
 * @param value an int
 * @return current
 */
fun Request.withInt(key: String?, value: Int): Request {
    putExtra(key, value)
    return this
}

/**
 * Inserts a long value into the mapping of this Bundle, replacing
 * any existing value for the given key.
 *
 * @param key   a String, or null
 * @param value a long
 * @return current
 */
fun Request.withLong(key: String, value: Long): Request {
    putExtra(key, value)
    return this
}

/**
 * Inserts a double value into the mapping of this Bundle, replacing
 * any existing value for the given key.
 *
 * @param key   a String, or null
 * @param value a double
 * @return current
 */
fun Request.withDouble(key: String, value: Double): Request {
    putExtra(key, value)
    return this
}

/**
 * Inserts a byte value into the mapping of this Bundle, replacing
 * any existing value for the given key.
 *
 * @param key   a String, or null
 * @param value a byte
 * @return current
 */
fun Request.withByte(key: String, value: Byte): Request {
    putExtra(key, value)
    return this
}

/**
 * Inserts a char value into the mapping of this Bundle, replacing
 * any existing value for the given key.
 *
 * @param key   a String, or null
 * @param value a char
 * @return current
 */
fun Request.withChar(key: String, value: Char): Request {
    putExtra(key, value)
    return this
}

/**
 * Inserts a float value into the mapping of this Bundle, replacing
 * any existing value for the given key.
 *
 * @param key   a String, or null
 * @param value a float
 * @return current
 */
fun Request.withFloat(key: String, value: Float): Request {
    putExtra(key, value)
    return this
}

/**
 * Inserts a CharSequence value into the mapping of this Bundle, replacing
 * any existing value for the given key.  Either key or value may be null.
 *
 * @param key   a String, or null
 * @param value a CharSequence, or null
 * @return current
 */
fun Request.withCharSequence(
    key: String,
    value: CharSequence?
): Request {
    putExtra(key, value)
    return this
}

/**
 * Inserts a Parcelable value into the mapping of this Bundle, replacing
 * any existing value for the given key.  Either key or value may be null.
 *
 * @param key   a String, or null
 * @param value a Parcelable object, or null
 * @return current
 */
fun Request.withParcelable(
    key: String,
    value: Parcelable?
): Request {
    putExtra(key, value)
    return this
}

/**
 * Inserts an array of Parcelable values into the mapping of this Bundle,
 * replacing any existing value for the given key.  Either key or value may
 * be null.
 *
 * @param key   a String, or null
 * @param value an array of Parcelable objects, or null
 * @return current
 */
fun Request.withParcelableArray(
    key: String,
    value: Array<Parcelable?>?
): Request {
    putExtra(key, value)
    return this
}

/**
 * Inserts a List of Parcelable values into the mapping of this Bundle,
 * replacing any existing value for the given key.  Either key or value may
 * be null.
 *
 * @param key   a String, or null
 * @param value an ArrayList of Parcelable objects, or null
 * @return current
 */
fun Request.withParcelableArrayList(
    key: String,
    value: ArrayList<out Parcelable?>?
): Request {
    putParcelableArrayList(key, value)
    return this
}

/**
 * Inserts a SparceArray of Parcelable values into the mapping of this
 * Bundle, replacing any existing value for the given key.  Either key
 * or value may be null.
 *
 * @param key   a String, or null
 * @param value a SparseArray of Parcelable objects, or null
 * @return current
 */
fun Request.withSparseParcelableArray(
    key: String,
    value: SparseArray<out Parcelable?>?
): Request {
    putParcelableSparseArray(key, value)
    return this
}

/**
 * Inserts an ArrayList value into the mapping of this Bundle, replacing
 * any existing value for the given key.  Either key or value may be null.
 *
 * @param key   a String, or null
 * @param value an ArrayList object, or null
 * @return current
 */
fun Request.withIntegerArrayList(
    key: String,
    value: ArrayList<Int?>?
): Request {
    putIntegerArrayList(key, value)
    return this
}

/**
 * Inserts an ArrayList value into the mapping of this Bundle, replacing
 * any existing value for the given key.  Either key or value may be null.
 *
 * @param key   a String, or null
 * @param value an ArrayList object, or null
 * @return current
 */
fun Request.withStringArrayList(
    key: String,
    value: ArrayList<String?>?
): Request {
    putStringArrayList(key, value)
    return this
}

/**
 * Inserts an ArrayList value into the mapping of this Bundle, replacing
 * any existing value for the given key.  Either key or value may be null.
 *
 * @param key   a String, or null
 * @param value an ArrayList object, or null
 * @return current
 */
fun Request.withCharSequenceArrayList(
    key: String,
    value: ArrayList<CharSequence?>?
): Request {
    putCharSequenceArrayList(key, value)
    return this
}

/**
 * Inserts a Serializable value into the mapping of this Bundle, replacing
 * any existing value for the given key.  Either key or value may be null.
 *
 * @param key   a String, or null
 * @param value a Serializable object, or null
 * @return current
 */
fun Request.withSerializable(
    key: String,
    value: Serializable?
): Request {
    putExtra(key, value)
    return this
}

/**
 * Inserts a byte array value into the mapping of this Bundle, replacing
 * any existing value for the given key.  Either key or value may be null.
 *
 * @param key   a String, or null
 * @param value a byte array object, or null
 * @return current
 */
fun Request.withByteArray(
    key: String,
    value: ByteArray?
): Request {
    putExtra(key, value)
    return this
}

/**
 * Inserts a short array value into the mapping of this Bundle, replacing
 * any existing value for the given key.  Either key or value may be null.
 *
 * @param key   a String, or null
 * @param value a short array object, or null
 * @return current
 */
fun Request.withShortArray(
    key: String,
    value: ShortArray?
): Request {
    putExtra(key, value)
    return this
}

/**
 * Inserts a char array value into the mapping of this Bundle, replacing
 * any existing value for the given key.  Either key or value may be null.
 *
 * @param key   a String, or null
 * @param value a char array object, or null
 * @return current
 */
fun Request.withCharArray(
    key: String,
    value: CharArray?
): Request {
    putExtra(key, value)
    return this
}

/**
 * Inserts a float array value into the mapping of this Bundle, replacing
 * any existing value for the given key.  Either key or value may be null.
 *
 * @param key   a String, or null
 * @param value a float array object, or null
 * @return current
 */
fun Request.withFloatArray(
    key: String,
    value: FloatArray?
): Request {
    putExtra(key, value)
    return this
}

/**
 * Inserts a CharSequence array value into the mapping of this Bundle, replacing
 * any existing value for the given key.  Either key or value may be null.
 *
 * @param key   a String, or null
 * @param value a CharSequence array object, or null
 * @return current
 */
fun Request.withCharSequenceArray(
    key: String,
    value: Array<CharSequence?>?
): Request {
    putExtra(key, value)
    return this
}

/**
 * Inserts a Bundle value into the mapping of this Bundle, replacing
 * any existing value for the given key.  Either key or value may be null.
 *
 * @param key   a String, or null
 * @param value a Bundle object, or null
 * @return current
 */
fun Request.withBundle(
    key: String,
    value: Bundle?
): Request {
    putExtra(key, value)
    return this
}

/**
 * Set normal transition anim
 *
 * @param enterAnim enter
 * @param exitAnim  exit
 * @return current
 */
fun Request.withTransition(enterAnim: Int, exitAnim: Int): Request {
    putExtra(START_ACTIVITY_ANIMATION, intArrayOf(exitAnim, exitAnim))
    return this
}


/**
 * Navigation to the route with path in postcard.
 * No param, will be use application context.
 */
fun Request.navigation() {
    start()
}

/**
 * Navigation to the route with path in postcard.
 *
 * @param context Activity and so on.
 */
fun Request.navigation(context: Context?) {
    start(context, null)
}

/**
 * Navigation to the route with path in postcard.
 *
 * @param context Activity and so on.
 */
fun Request.navigation(context: Context?, callback: RouterCallback?) {
    start(context, callback)
}
