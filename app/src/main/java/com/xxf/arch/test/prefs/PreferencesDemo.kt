package com.xxf.arch.test.prefs

import com.xxf.ktx.SharedPreferencesOwner
import com.xxf.ktx.preferencesBinding
import com.xxf.ktx.randomUUIDString32
import com.xxf.ktx.standard.observable

object PreferencesDemo : SharedPreferencesOwner {
    var name: String by preferencesBinding("key", "xxx")
    //可以监听
    var name2: String by preferencesBinding("key2", "xxx").observable { property, newValue ->
        println("=============>PrefsDemo3:$newValue")
    }

    fun test() {
        println("=============>PrefsDemo:$name")
        name = randomUUIDString32;
        println("=============>PrefsDemo2:$name")
        name2 = randomUUIDString32;
        println("=============>PrefsDemo4:$name2")
    }
}