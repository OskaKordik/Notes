package com.natife.streaming.ext

import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.url(url: String) {
    Glide.with(this)
        .load(url)
        .override(this.width, this.height)
        .into(this)
}