package com.natife.streaming.ext

import java.text.SimpleDateFormat
import java.util.*

fun Date.toRequest(): String{
    val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault())
    return sdf.format(this)
}