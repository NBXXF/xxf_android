package com.xxf.arch.test.prefs

import com.google.gson.reflect.TypeToken
import com.xxf.json.Json
import com.xxf.ktx.IPreferencesOwner
import com.xxf.ktx.PrefsDelegate
import com.xxf.ktx.SharedPreferencesOwner
import com.xxf.ktx.preferencesBinding
import com.xxf.ktx.randomUUIDString32
import com.xxf.ktx.standard.KeyValueDelegate
import com.xxf.ktx.standard.observable
import kotlin.reflect.KProperty

fun <P : IPreferencesOwner, V> PrefsDelegate<P, V>.useGson(typeToken: TypeToken<V>): KeyValueDelegate<P, V> {
    return object : KeyValueDelegate<P, V>(this.key, this.default) {
        val stringDelegate = PrefsDelegate<P, String>(this.key, "{}", String::class);

        @Suppress("UNCHECKED_CAST")
        override fun getValue(thisRef: P, property: KProperty<*>): V {
            val gson = Json.innerDefaultGson
            return gson.fromJson(
                stringDelegate.getValue(thisRef, property),
                typeToken
            ) ?: default
        }

        override fun setValue(thisRef: P, property: KProperty<*>, value: V) {
            val gson = Json.innerDefaultGson
            stringDelegate.setValue(thisRef, property, gson.toJson(value));
        }
    }
}

object PreferencesDemo : SharedPreferencesOwner {

    data class User(val name: String? = null)

    var name: String by preferencesBinding("key", "xxx")

    //可以监听
    var name2: String by preferencesBinding("key2", "xxx").observable { property, newValue ->
        println("=============>PrefsDemo3:$newValue")
    }
    var user: User by preferencesBinding("key3", User()).useGson(TypeToken.get(User::class.java))

    fun test() {
        println("=============>PrefsDemo:$name")
        name = randomUUIDString32;
        println("=============>PrefsDemo2:$name")
        name2 = randomUUIDString32;
        println("=============>PrefsDemo4:$name2")

        user = User("张三")
        println("=============>PrefsDemoUser:$user")
    }
}