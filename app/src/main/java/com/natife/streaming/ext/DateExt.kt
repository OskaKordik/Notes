package com.natife.streaming.ext

import java.text.SimpleDateFormat
import java.util.*

fun Date.toRequest(): String{
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
    return sdf.format(this)
}

fun String.fromResponse(): Date{
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    return sdf.parse(this)
}