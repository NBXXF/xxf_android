package com.xxf.arch.test.prefs

import com.google.gson.JsonNull
import com.xxf.json.Json
import com.xxf.ktx.IPreferencesOwner
import com.xxf.ktx.PrefsDelegate
import com.xxf.ktx.SharedPreferencesOwner
import com.xxf.ktx.writeAsync
import com.xxf.ktx.observable
import com.xxf.ktx.preferencesBinding
import com.xxf.ktx.randomUUIDString32
import kotlin.reflect.KProperty

inline fun <P : IPreferencesOwner, reified V> PrefsDelegate<P, out V>.useGson(): PrefsDelegate<P, V> {
    val delegate = this
    return object : PrefsDelegate<P, V>(this.key, this.default, V::class) {

        override fun getValue(thisRef: P, property: KProperty<*>): V {
            val value =
                PrefsDelegate<P, String?>(this.key, "", String::class).getValue(thisRef, property)
            return if (value.isNullOrEmpty()) {
                default
            } else {
                Json.fromJson<V>(value) ?: default
            }
        }

        override fun setValue(thisRef: P, property: KProperty<*>, value: Any?) {
            if (value is JsonNull || value == null) {
                delegate.setValue(thisRef, property, null)
            } else {
                delegate.setValue(thisRef, property, Json.toJson(value))
            }
        }
    }
}

object PreferencesDemo : SharedPreferencesOwner {

    data class User(val name: String? = null)

    var name: String by preferencesBinding("key", "xxx").writeAsync().observable { property, newValue ->
        println("=============>PrefsDemo change:$newValue")
    }

    //可以监听
    var name2: String by preferencesBinding("key2", "xxx").observable { property, newValue ->
        println("=============>PrefsDemo3:$newValue")
    }
    var user: User by preferencesBinding("key3", User()).useGson()

    fun test() {
        println("=============>PrefsDemo:$name")
        name = randomUUIDString32;
        println("=============>PrefsDemo2:$name")
        name2 = randomUUIDString32;
        println("=============>PrefsDemo4:$name2")

        println("=============>PrefsDemoUserBefore:$user")
        user = User("张三 ${System.currentTimeMillis()}")
        println("=============>PrefsDemoUser:$user")
    }
}

