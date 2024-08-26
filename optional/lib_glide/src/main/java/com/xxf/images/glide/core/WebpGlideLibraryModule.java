package com.xxf.images.glide.core;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.webp.decoder.ByteBufferWebpDecoder;
import com.bumptech.glide.integration.webp.decoder.StreamWebpDecoder;
import com.bumptech.glide.integration.webp.decoder.WebpDrawable;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.module.LibraryGlideModule;

import java.io.InputStream;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080
 * Date: 1/26/21 4:16 PM
 * Description: 支持glide加载webp动图
 */
@GlideModule
public class WebpGlideLibraryModule extends LibraryGlideModule {
    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        super.registerComponents(context, glide, registry);
        BitmapPool bitmapPool = glide.getBitmapPool();
        ArrayPool arrayPool = glide.getArrayPool();
        ByteBufferWebpDecoder byteBufferWebpDecoder = new ByteBufferWebpDecoder(context, arrayPool, bitmapPool);
        registry.prepend(InputStream.class, WebpDrawable.class, new StreamWebpDecoder(byteBufferWebpDecoder, arrayPool));
    }
}
