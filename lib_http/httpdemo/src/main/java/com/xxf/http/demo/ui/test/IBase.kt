package com.xxf.http.demo.ui.test

interface IBase {
    var name: String?
}
interface IBase2:IBase {
}

class Animal() : IBase,IBase2 {
    override var name: String? = null
    var desc: String? = null
    override fun toString(): String {
        return "Animal(name=$name, desc=$desc)"
    }
}

class Person : IBase {
    override var name: String? = null
    var bite: String? = null

    override fun toString(): String {
        return "Person(name=$name, bite=$bite)"
    }

}