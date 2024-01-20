package com.xxf.pinyin

import android.content.Context
import com.github.promeg.pinyinhelper.Pinyin
import com.github.promeg.tinypinyin.lexicons.android.cncity.CnCityDict

private var addedCnCityDict: Boolean = false
private fun addCnCityDict(context: Context) {
    if (!addedCnCityDict) {
        Pinyin.init(Pinyin.newConfig().with(CnCityDict.getInstance(context)))
        addedCnCityDict = true
    }
}

/**
 * 将输入字符串转为拼音，转换过程中会使用之前设置的用户词典，以字符为单位插入分隔符
 * 例: "hello:中国"  在separator为","时，输出： "h,e,l,l,o,:,ZHONG,GUO,!"
 *
 * @param separator 转换过程中会使用之前设置的用户词典，以字符为单位插入分隔符
 */
fun String.toPinyin(context: Context, separator: String = ""): String {
    addCnCityDict(context.applicationContext)
    return Pinyin.toPinyin(this, separator)
}

/**
 * 将输入字符转为拼音
 */
fun Char.toPinyin(context: Context): String {
    addCnCityDict(context.applicationContext)
    return Pinyin.toPinyin(this)
}

/**
 * 判断输入字符是否为汉字
 */
fun Char.isChinese(context: Context): Boolean {
    addCnCityDict(context.applicationContext)
    return Pinyin.isChinese(this)
}