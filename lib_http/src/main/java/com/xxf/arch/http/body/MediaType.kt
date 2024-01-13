package com.xxf.arch.http.body

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import java.nio.charset.Charset
import java.util.regex.Pattern

val MediaType.Companion.Json: MediaType by lazy {
    "application/json".toMediaType()
}
val MediaType.Companion.JsonUTF8: MediaType by lazy {
    "application/json; charset=utf-8".toMediaType()
}
val MediaType.Companion.FormData: MediaType by lazy {
    "multipart/form-data".toMediaType()
}
val MediaType.Companion.FormUrlEncoded: MediaType by lazy {
    "application/x-www-form-urlencoded".toMediaType()
}
val MediaType.Companion.Xml: MediaType by lazy {
    "application/xml".toMediaType()
}

/**
 * 增加builder模式
 */
fun MediaType.newBuilder(): MediaTypeBuilder {
    return MediaTypeBuilder(this)
}

class MediaTypeBuilder(val from: MediaType) {

    companion object {
        private const val TOKEN = "([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)"
        private const val QUOTED = "\"([^\"]*)\""
        private val TYPE_SUBTYPE = Pattern.compile("${TOKEN}/${TOKEN}")
        private val PARAMETER = Pattern.compile(";\\s*(?:${TOKEN}=(?:${TOKEN}|${QUOTED}))?")

        private fun String.parseParameterNamesAndValues(): MutableList<String> {
            val typeSubtype = TYPE_SUBTYPE.matcher(this)
            require(typeSubtype.lookingAt()) { "No subtype found for: \"$this\"" }
            val parameterNamesAndValues = mutableListOf<String>()
            val parameter = PARAMETER.matcher(this)
            var s = typeSubtype.end()
            while (s < length) {
                parameter.region(s, length)
                require(parameter.lookingAt()) {
                    "Parameter is not formatted correctly: \"${substring(s)}\" for: \"$this\""
                }

                val name = parameter.group(1)
                if (name == null) {
                    s = parameter.end()
                    continue
                }

                val token = parameter.group(2)
                val value = when {
                    token == null -> {
                        // Value is "double-quoted". That's valid and our regex group already strips the quotes.
                        parameter.group(3)
                    }

                    token.startsWith("'") && token.endsWith("'") && token.length > 2 -> {
                        // If the token is 'single-quoted' it's invalid! But we're lenient and strip the quotes.
                        token.substring(1, token.length - 1)
                    }

                    else -> token
                }

                parameterNamesAndValues += name
                parameterNamesAndValues += value
                s = parameter.end()
            }
            return parameterNamesAndValues;
        }
    }

    /**
     * Returns the high-level media type, such as "text", "image", "audio", "video", or "application".
     */
    private var type: String = from.type

    /**
     * Returns a specific media subtype, such as "plain" or "png", "mpeg", "mp4" or "xml".
     */
    private var subtype: String = from.subtype;

    private var parameterNamesAndValues: MutableMap<String, Any> =
        mutableMapOf<String, Any>().apply {
            val parameterNamesAndValues = from.toString().parseParameterNamesAndValues()
            for (i in parameterNamesAndValues.indices step 2) {
                put(parameterNamesAndValues[i], parameterNamesAndValues[i + 1])
            }
        }


    fun charset(value: Charset? = from.charset()) = apply {
        value?.name()?.also { parameterNamesAndValues["charset"] = it }
    }

    fun type(value: String) = apply {
        this.type = value
    }

    fun subtype(value: String) = apply {
        this.subtype = value
    }

    fun parameter(name: String, value: String) = apply {
        parameterNamesAndValues[name] = value
    }

    fun build(): MediaType {
        return "$type/$subtype".run {
            if (parameterNamesAndValues.isEmpty()) {
                ""
            } else {
                val separator = "; "
                separator + parameterNamesAndValues.map { "${it.key}=${it.value}" }
                    .joinToString(separator)
            }
        }.toMediaType()
    }
}