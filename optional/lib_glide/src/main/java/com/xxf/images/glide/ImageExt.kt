package com.xxf.images.glide

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder


@JvmOverloads
fun ImageView.load(
    model: Any,
    block: (request: RequestBuilder<Drawable>) -> Unit
) {
    Glide.with(this)
        .load(model)
        .also {
            block(it)
        }
        .into(this)
}

@JvmOverloads
fun ImageView.load(
    model: Any,
    @RawRes @DrawableRes placeHolder: Int,
    @RawRes @DrawableRes errorId: Int = placeHolder,
    block: (request: RequestBuilder<Drawable>) -> Unit
) {
    Glide.with(this)
        .load(model)
        .placeholder(placeHolder)
        .error(errorId)
        .also {
            block(it)
        }
        .into(this)
}