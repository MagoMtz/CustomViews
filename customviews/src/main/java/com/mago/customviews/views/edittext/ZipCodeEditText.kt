package com.mago.customviews.views.edittext

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.graphics.toRectF
import com.mago.customviews.R

class ZipCodeEditText(context: Context, attributeSet: AttributeSet):
    AppCompatEditText(context, attributeSet) {

    var isMandatory: Boolean = false
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }

    private val framePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.border)
        style = Paint.Style.STROKE
        strokeWidth = 3F
    }

    private val frameAlertPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.frame_invalid)
        style = Paint.Style.STROKE
        strokeWidth = 3F
    }

    init {
        context.theme.obtainStyledAttributes(attributeSet, R.styleable.ZipCodeEditText, 0, 0)
            .apply {
                try {
                    isMandatory = getBoolean(R.styleable.ZipCodeEditText_isMandatory, false)

                    background = null
                } finally {
                    recycle()
                }
            }
        inputType = InputType.TYPE_CLASS_PHONE
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val isValid = text.toString().length == 5

        canvas?.apply {
            val cBounds = clipBounds
            cBounds.inset(0, 8)

            if (isMandatory)
                if (!isValid)
                    drawRoundRect(cBounds.toRectF(), 10F, 10F, frameAlertPaint)
                else
                    drawRoundRect(cBounds.toRectF(), 10F, 10F, framePaint)
            else
                drawRoundRect(cBounds.toRectF(), 10F, 10F, framePaint)
        }

    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        addTextChangedListener(textWatcher())
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        removeTextChangedListener(textWatcher())
    }

    private fun textWatcher(): TextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            s?.let { editable ->
                val count = editable.length
                if (count >= 6) {
                    val text = s.substring(0, count - 1)
                    setText(text)
                    setSelection(count - 1)
                }
            }
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    }

}