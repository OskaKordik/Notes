package com.natife.streaming.ext

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.constraintlayout.widget.Placeholder
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.request.RequestListener

fun ImageView.url(url: String) {
    Glide.with(this)
        .load(url)
        .override(this.width, this.height)
        .into(this)
}
fun ImageView.url(url: String,placeholder: String) {
    Glide.with(this)
        .load(url)
        .override(this.width, this.height)
        .error(Glide.with(this).load(placeholder).override(this.width, this.height))
        .into(this)
}