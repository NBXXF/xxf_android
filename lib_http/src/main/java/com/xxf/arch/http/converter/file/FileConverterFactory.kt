package com.xxf.arch.http.converter.file

import android.content.res.AssetFileDescriptor
import android.net.Uri
import android.os.ParcelFileDescriptor
import okhttp3.RequestBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.Part
import java.io.File
import java.io.FileDescriptor
import java.io.InputStream
import java.lang.reflect.Type

/**
 * @Part @Body
 * 支持上传对象类型为 File ByteArray InputStream FileDescriptor ParcelFileDescriptor AssetFileDescriptor Uri
 */
class FileConverterFactory : Converter.Factory() {

    companion object {
        fun create(): FileConverterFactory {
            return FileConverterFactory()
        }
    }

    private fun checkAnnotations(
        parameterAnnotations: Array<Annotation>,
        methodAnnotations: Array<Annotation>
    ): Boolean {
        return parameterAnnotations.firstOrNull { it is Part || it is Body } != null
    }

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<Annotation>,
        methodAnnotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody>? {
        if (type === File::class.java) {
            if (!checkAnnotations(parameterAnnotations, methodAnnotations)) {
                return null
            }
            return FileRequestBodyConverter()
        } else if (type == ByteArray::class.java) {
            if (!checkAnnotations(parameterAnnotations, methodAnnotations)) {
                return null
            }
            return ByteArrayRequestBodyConverter()
        } else if (type == InputStream::class.java || InputStream::class.java.isAssignableFrom(type as Class<*>)) {
            if (!checkAnnotations(parameterAnnotations, methodAnnotations)) {
                return null
            }
            return InputStreamRequestBodyConverter()
        } else if ((type === FileDescriptor::class.java) || FileDescriptor::class.java.isAssignableFrom(
                type
            )
        ) {
            if (!checkAnnotations(parameterAnnotations, methodAnnotations)) {
                return null
            }
            return FileDescriptorRequestBodyConverter()
        } else if ((type == ParcelFileDescriptor::class.java) || ParcelFileDescriptor::class.java.isAssignableFrom(
                type,
            )
        ) {
            if (!checkAnnotations(parameterAnnotations, methodAnnotations)) {
                return null
            }
            return ParcelFileDescriptorRequestBodyConverter()
        } else if ((type == AssetFileDescriptor::class.java) || AssetFileDescriptor::class.java.isAssignableFrom(
                type
            )
        ) {
            if (!checkAnnotations(parameterAnnotations, methodAnnotations)) {
                return null
            }
            return AssetFileDescriptorRequestBodyConverter()
        } else if (type == Uri::class.java) {
            if (!checkAnnotations(parameterAnnotations, methodAnnotations)) {
                return null
            }
            return UriRequestBodyConverter()
        }
        return null
    }


}