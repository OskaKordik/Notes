package com.study.noteskotlin

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText

class LinedEditText(context: Context?, attrs: AttributeSet?) : AppCompatEditText(context, attrs) {

    private var mPaint: Paint = Paint()
    private var mRect: Rect = Rect()

    init {
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = 1F
        mPaint.color = resources.getColor(R.color.darkYellow)
    }

    override fun onDraw(canvas: Canvas?) {
        val height = (this.parent as View).height
        val lineHeight = lineHeight
        val numberOfLines = height / lineHeight
        val rect = mRect
        val paint = mPaint
        var baseline = getLineBounds(0, rect)

        for (i in 0 until numberOfLines) {
            canvas!!.drawLine(
                rect.left.toFloat(),
                (baseline + 5).toFloat(),
                rect.right.toFloat(),
                (baseline + 5).toFloat(),
                paint
            )
            baseline += lineHeight
        }

        super.onDraw(canvas)
    }
}