package com.xxf.images.glide

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder


inline fun ImageView.load(
    model: Any,
    builder: RequestBuilder<Drawable>.() -> Unit
) {
    Glide.with(this)
        .load(model)
        .apply(builder)
        .into(this)
}

inline fun ImageView.load(
    model: Any,
    @RawRes @DrawableRes placeHolder: Int,
    @RawRes @DrawableRes errorId: Int = placeHolder,
    builder: RequestBuilder<Drawable>.() -> Unit
) {

    Glide.with(this)
        .load(model)
        .placeholder(placeHolder)
        .error(errorId)
        .apply(builder)
        .into(this)
}

inline fun ImageView.dispose() {
    Glide.with(this).clear(this)
}