package com.study.noteskotlin.utils

import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView

class VerticalSpacingItemDecorator(private val verticalSpaceHeight : Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, itemPosition: Int, parent: RecyclerView) {
        outRect.bottom = verticalSpaceHeight
    }
}