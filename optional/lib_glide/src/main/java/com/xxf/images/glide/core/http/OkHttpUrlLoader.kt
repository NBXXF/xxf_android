package com.xxf.images.glide.core.http

import android.util.Log
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoader.LoadData
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory

import com.xxf.arch.http.OkHttpClientBuilder
import com.xxf.arch.http.interceptor.HttpLoggingInterceptor
import com.xxf.application.application
import com.xxf.ktx.isAppDebug
import okhttp3.Call
import java.io.InputStream

/**
 * A simple model loader for fetching media over http/https using OkHttp.
 */
class OkHttpUrlLoader(private val client: Call.Factory) : ModelLoader<GlideUrl, InputStream> {
    class ImageHttpLoggerInterceptor :
        HttpLoggingInterceptor(Logger { message -> Log.d(TAG, "===============>image:$message") }) {
        init {
            level = if (application.isAppDebug) Level.HEADERS else Level.NONE
        }
    }

    companion object {
        var okHttpClient: Call.Factory = OkHttpClientBuilder()
            //需要鉴权next_space
            // .addInterceptor(BaseHttpHeaderInterceptor())
            // .addInterceptor(com.persagy.visitor.images.glide.ImageLoadRetryInterceptor())
            .addInterceptor(ImageHttpLoggerInterceptor())
            .build()
        const val TAG = "Glide"
    }

    override fun handles(url: GlideUrl): Boolean {
        return true
    }


    override fun buildLoadData(
        model: GlideUrl,
        width: Int,
        height: Int,
        options: Options
    ): LoadData<InputStream> {
        return LoadData(model, OkHttpStreamFetcher(client, model))
    }

    /**
     * The default factory for [OkHttpUrlLoader]s.
     */
    // Public API.
    class Factory
    /**
     * Constructor for a new Factory that runs requests using a static singleton client.
     */ @JvmOverloads constructor() :
        ModelLoaderFactory<GlideUrl, InputStream> {
        override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<GlideUrl, InputStream> {
            return OkHttpUrlLoader(okHttpClient)
        }

        override fun teardown() {
            // Do nothing, this instance doesn't own the client.
        }
    }
}