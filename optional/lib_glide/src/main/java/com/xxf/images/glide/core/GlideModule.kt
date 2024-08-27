package com.xxf.images.glide.core

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.graphics.drawable.PictureDrawable
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.webp.decoder.ByteBufferWebpDecoder
import com.bumptech.glide.integration.webp.decoder.StreamWebpDecoder
import com.bumptech.glide.integration.webp.decoder.WebpDrawable
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.engine.cache.DiskCache.Factory.DEFAULT_DISK_CACHE_SIZE
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.target.ViewTarget
import com.caverock.androidsvg.SVG
import com.xxf.images.glide.svg.SvgDecoder
import com.xxf.images.glide.svg.SvgDrawableTranscoder
import java.io.InputStream


@GlideModule(glideName = "IGlideModule")
class GlideModule : AppGlideModule() {

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        super.registerComponents(context, glide, registry)
        val bitmapPool = glide.bitmapPool
        val arrayPool = glide.arrayPool
        val byteBufferWebpDecoder = ByteBufferWebpDecoder(context, arrayPool, bitmapPool)
        registry.prepend(
            InputStream::class.java,
            WebpDrawable::class.java, StreamWebpDecoder(byteBufferWebpDecoder, arrayPool)
        )

        /**
         * 注册svg 项目需要
         */
        registry
            .register(
                SVG::class.java, PictureDrawable::class.java,
                SvgDrawableTranscoder()
            )
            .append(
                InputStream::class.java, SVG::class.java,
                SvgDecoder()
            )
    }

    override fun isManifestParsingEnabled(): Boolean {
        return false
    }

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        super.applyOptions(context, builder)

        //磁盘缓存设置大一些 保证永久缓存
        val yourSizeInBytes = DEFAULT_DISK_CACHE_SIZE * 10L;
        builder.setDiskCache(InternalCacheDiskCacheFactory(context, yourSizeInBytes))

        //内存大小设置
//        val calculator = MemorySizeCalculator.Builder(context).build()
//        val defaultMemoryCacheSize = calculator.memoryCacheSize
//        val defaultBitmapPoolSize = calculator.bitmapPoolSize
//        val customMemoryCacheSize = (1.5 * defaultMemoryCacheSize).toInt()
//        val customBitmapPoolSize = (1.5 * defaultBitmapPoolSize).toInt()
//        builder.setMemoryCache(LruResourceCache(customMemoryCacheSize.toLong()))
//        builder.setBitmapPool(LruBitmapPool(customBitmapPoolSize.toLong()))


        builder.addGlobalRequestListener(object : RequestListener<Any> {

            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Any>,
                isFirstResource: Boolean
            ): Boolean {
                return false
            }

            override fun onResourceReady(
                resource: Any,
                model: Any,
                target: Target<Any>?,
                dataSource: DataSource,
                isFirstResource: Boolean
            ): Boolean {
                try {
                    val heightWidthRatio: Float
                    if (resource is Bitmap) {
                        heightWidthRatio = resource.height * 1.0f / resource.width
                    } else if (resource is Drawable) {
                        heightWidthRatio = resource.intrinsicHeight * 1.0f / resource.intrinsicWidth
                    } else {
                        heightWidthRatio = 1.0f
                    }
                    if (heightWidthRatio >= 3.0f || heightWidthRatio <= 0.3f) {
                        /**
                         * 解决 glide 长款比例过大的时候导致的崩溃的问题
                         * https://github.com/bumptech/glide/issues/2990
                         * 关闭硬件加速
                         */
                        val targetView = when (target) {
                            is ViewTarget<*, *> -> target.view
                            is CustomViewTarget<*, *> -> target.view
                            else -> null
                        }

//                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P || targetView is PhotoView) {
//                            targetView?.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
//                        }

                        targetView?.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
                return false
            }

        })
    }
}