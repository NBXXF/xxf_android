package com.xxf.utils

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.text.TextUtils
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.xxf.utils.BitmapUtils.grey
import com.xxf.utils.BitmapUtils.recycle
import com.xxf.utils.BitmapUtils.scale
import com.xxf.utils.BitmapUtils.toByte
import java.io.File
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import java.util.*

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description 资源文件工具类
 */
object ResourcesUtil {
    fun getString(context: Context, @StringRes resId: Int): String {
        return context.getString(resId)
    }

    fun getString(context: Context, @StringRes resId: Int, vararg formatArgs: Any?): String {
        return context.getString(resId, *formatArgs)
    }

    fun getDrawable(context: Context?, @DrawableRes resId: Int): Drawable? {
        return ContextCompat.getDrawable(context!!, resId)
    }

    @ColorInt
    fun getColor(context: Context?, @ColorRes resId: Int): Int {
        return ContextCompat.getColor(context!!, resId)
    }

    fun getIdByName(context: Context, name: String?): Int {
        return context.resources.getIdentifier(name, "id", context.packageName)
    }

    fun getStringIdByName(context: Context, name: String?): Int {
        return context.resources.getIdentifier(name, "string", context.packageName)
    }

    fun getColorIdByName(context: Context, name: String?): Int {
        return context.resources.getIdentifier(name, "color", context.packageName)
    }

    fun getDimenIdByName(context: Context, name: String?): Int {
        return context.resources.getIdentifier(name, "dimen", context.packageName)
    }

    fun getDrawableIdByName(context: Context, name: String?): Int {
        return context.resources.getIdentifier(name, "drawable", context.packageName)
    }

    fun getMipmapIdByName(context: Context, name: String?): Int {
        return context.resources.getIdentifier(name, "mipmap", context.packageName)
    }

    fun getLayoutIdByName(context: Context, name: String?): Int {
        return context.resources.getIdentifier(name, "layout", context.packageName)
    }

    fun getStyleIdByName(context: Context, name: String?): Int {
        return context.resources.getIdentifier(name, "style", context.packageName)
    }

    fun getAnimIdByName(context: Context, name: String?): Int {
        return context.resources.getIdentifier(name, "anim", context.packageName)
    }

    fun getMenuIdByName(context: Context, name: String?): Int {
        return context.resources.getIdentifier(name, "menu", context.packageName)
    }

    fun copyFileFromAssets(context: Context, assetsFilePath: String, destFilePath: String): Boolean {
        var res = true
        try {
            val assets = context.assets.list(assetsFilePath)
            if (assets != null && assets.size > 0) {
                for (asset in assets) {
                    res = res and copyFileFromAssets(context, "$assetsFilePath/$asset", "$destFilePath/$asset")
                }
            } else {
                res = FileUtils.writeFileFromIS(
                        File(destFilePath),
                        context.assets.open(assetsFilePath), false
                )
            }
        } catch (e: IOException) {
            e.printStackTrace()
            res = false
        }
        return res
    }

    @JvmOverloads
    fun readAssets2String(context: Context, assetsFilePath: String?, charsetName: String? = null): String {
        return try {
            val `is` = context.assets.open(assetsFilePath!!)
            val bytes = FileUtils.getFileBytes(`is`) ?: return ""
            if (TextUtils.isEmpty(charsetName)) {
                String(bytes)
            } else {
                try {
                    String(bytes, Charset.forName(charsetName))
                } catch (e: UnsupportedEncodingException) {
                    e.printStackTrace()
                    ""
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            ""
        }
    }

    /**
     * 检查所有
     * 主module app R文件的内容是已经合并好的,但是有第三方依赖库里面的资源重复
     * 检查资源重复 第一步只检查字符串
     * 未来:id 颜色等等,请期待
     * 避免lint lint缺陷就是能忽略检查
     */
    fun checkResources(context: Context, ignoreIds: List<Int>?) {
        var aClass: Class<*>? = null
        try {
            aClass = Class.forName(context.packageName + ".R")
        } catch (ex: ClassNotFoundException) {
            ex.printStackTrace()
        }
        if (aClass == null) {
            return
        }
        val stringResources = getStringResources(aClass)
        stringResources.removeAll(ignoreIds!!)
        /**
         * 字符串
         * key:string values:ids集合
         */
        var stringRepeatMap: MutableMap<String?, MutableList<String?>?>? = HashMap()
        for (id in stringResources) {
            val key = getString(context, id)
            var idNames = stringRepeatMap!![key]
            if (idNames == null) {
                idNames = ArrayList()
            }
            idNames.add(context.resources.getResourceEntryName(id))
            stringRepeatMap[key] = idNames
        }
        val drawableResources = getDrawableResources(aClass)
        drawableResources.removeAll(ignoreIds)
        /**
         * 图片
         * key:drawable values:ids集合
         */
        var drawableRepeatMap: MutableMap<String?, MutableList<String?>?>? = HashMap()
        for (id in drawableResources) {
            /**
             * 会存在load 不出来的xml的图片
             */
            var bitmap = BitmapFactory.decodeResource(context.resources, id)
            if (bitmap != null) {
                bitmap = scale(bitmap, 32, 32)
                bitmap = grey(bitmap)
            } else {
                /**
                 * 加载xml中的drawable
                 */
                //bitmap = BitmapUtils.drawableToBitmap(getDrawable(id));
            }
            if (bitmap == null) {
                continue
            }
            var bytes = toByte(bitmap)
            recycle(bitmap)
            var key: String? = null
            if (bytes != null) {
                try {
                    key = EncryptUtils.encryptMD5ToString(bytes)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                bytes = null
            }
            if (TextUtils.isEmpty(key)) {
                continue
            }
            var idNames = drawableRepeatMap!![key]
            if (idNames == null) {
                idNames = ArrayList()
            }
            idNames.add(context.resources.getResourceEntryName(id))
            drawableRepeatMap[key] = idNames
        }
        stringRepeatMap = convertRepeatMap(stringRepeatMap)
        val repeatBuilder = StringBuffer("String重复" + stringRepeatMap!!.size + "条:")
        for ((_, value) in stringRepeatMap) {
            repeatBuilder.append(value)
            repeatBuilder.append(";")
        }
        drawableRepeatMap = convertRepeatMap(drawableRepeatMap)
        repeatBuilder.append("image相似或重复" + drawableRepeatMap!!.size + "条:")
        for ((_, value) in drawableRepeatMap) {
            repeatBuilder.append(value)
            repeatBuilder.append(";")
        }
        if (!stringRepeatMap.isEmpty() || !drawableRepeatMap.isEmpty()) {
            throw RepeatResourcesException(repeatBuilder.toString())
        }
    }

    private fun convertRepeatMap(map: MutableMap<String?, MutableList<String?>?>?): MutableMap<String?, MutableList<String?>?>? {
        if (map != null) {
            val iterator: MutableIterator<Map.Entry<String?, List<String?>?>> = map.entries.iterator()
            while (iterator.hasNext()) {
                val next = iterator.next()
                if (next.value == null || next.value!!.size <= 1) {
                    iterator.remove()
                }
            }
        }
        return map
    }

    /**
     * 获取r文件的所有字符串资源ids
     *
     * @param rClazz
     * @return
     */
    @JvmStatic
    fun getStringResources(vararg rClazz: Class<*>): MutableList<Int> {
        val idList: MutableList<Int> = ArrayList()
        if (rClazz != null) {
            for (aClass in rClazz) {
                val innerClazz = aClass.declaredClasses
                for (claszInner in innerClazz) {
                    /**
                     * find string.class
                     */
                    if (!TextUtils.equals(claszInner.simpleName, "string")) {
                        continue
                    }
                    val fields = claszInner.declaredFields
                    for (field in fields) {
                        try {
                            val id = field.getInt(claszInner)
                            idList.add(id)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }
        return idList
    }

    /**
     * 获取指定R文件下面的所有drawable 资源ids
     *
     * @param rClazz
     * @return
     */
    @JvmStatic
    fun getDrawableResources(vararg rClazz: Class<*>): MutableList<Int> {
        val idList: MutableList<Int> = ArrayList()
        if (rClazz != null) {
            for (aClass in rClazz) {
                val innerClazz = aClass.declaredClasses
                for (claszInner in innerClazz) {
                    if (TextUtils.equals(claszInner.simpleName, "drawable") || TextUtils.equals(claszInner.simpleName, "mipmap")) {
                        val fields = claszInner.declaredFields
                        for (field in fields) {
                            try {
                                val id = field.getInt(claszInner)
                                idList.add(id)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            }
        }
        return idList
    }

    class RepeatResourcesException(message: String?) : RuntimeException(message)
}