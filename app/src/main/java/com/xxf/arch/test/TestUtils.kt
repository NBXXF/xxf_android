package com.xxf.arch.test

object TestUtils {
    fun format(fileName: String): String {
        return fileName.replace("[<>|:\"*?/.\\\\]".toRegex(),"")
    }
}