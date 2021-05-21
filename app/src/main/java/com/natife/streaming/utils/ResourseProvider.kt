package com.natife.streaming.utils

import android.content.Context
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
//new
interface ResourceProvider {
    fun getString(@StringRes res: Int, vararg args: Any): String
    fun getQuantityString(@PluralsRes res: Int, quantity: Int, vararg args: Any): String
}

class ResourceProviderImpl (private val context: Context) : ResourceProvider {
    override fun getString(res: Int, vararg args: Any): String = context.getString(res, *args)
    override fun getQuantityString(res: Int, quantity: Int, vararg args: Any): String =
        context.resources.getQuantityString(res, quantity, *args)
}