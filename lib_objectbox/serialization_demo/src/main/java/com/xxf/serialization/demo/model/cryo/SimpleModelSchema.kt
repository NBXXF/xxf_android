package com.xxf.serialization.demo.model.cryo

import io.protostuff.Input
import io.protostuff.Output
import io.protostuff.Schema

class SimpleModelSchema:Schema<SimpleModel> {
    override fun getFieldName(number: Int): String {
        TODO("Not yet implemented")
    }

    override fun getFieldNumber(name: String?): Int {
        TODO("Not yet implemented")
    }

    override fun isInitialized(message: SimpleModel?): Boolean {
        TODO("Not yet implemented")
    }

    override fun newMessage(): SimpleModel {
        TODO("Not yet implemented")
    }

    override fun messageName(): String {
        TODO("Not yet implemented")
    }

    override fun messageFullName(): String {
        TODO("Not yet implemented")
    }

    override fun typeClass(): Class<in SimpleModel> {
        TODO("Not yet implemented")
    }

    override fun mergeFrom(input: Input?, message: SimpleModel?) {
        TODO("Not yet implemented")
    }

    override fun writeTo(output: Output?, message: SimpleModel?) {
        TODO("Not yet implemented")
    }
}