package com.mago.customviews.views.edittext

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.graphics.toRectF
import com.mago.customviews.R

/**
 * @author by jmartinez
 * @since 05/02/2020.
 */
class TextArea(context: Context, attributeSet: AttributeSet) : AppCompatEditText(context, attributeSet) {
    // Attributes
    var isMandatory: Boolean = false
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }

    init {
        context.theme.obtainStyledAttributes(attributeSet, R.styleable.TextArea, 0, 0)
            .apply {
                try {
                    isMandatory = getBoolean(R.styleable.TextArea_isMandatory, false)
                } finally {
                    recycle()
                }
            }

        minLines = 1
        setLines(3)
        maxLines = 15
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val textIsEmpty = text.toString().isEmpty()

        canvas?.apply {
            val cBounds = clipBounds
            cBounds.inset(0, 8)

            background = if (isMandatory)
                if (textIsEmpty)
                    ContextCompat.getDrawable(context, R.drawable.bg_common_invalid)
                else
                    ContextCompat.getDrawable(context, R.drawable.bg_common)
            else
                ContextCompat.getDrawable(context, R.drawable.bg_common)

        }
    }
}