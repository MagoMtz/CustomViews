package com.mago.customviews.views.edittext

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.text.InputType
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.graphics.toRectF
import com.mago.customviews.R
import com.mago.customviews.util.RegexPattern
import java.util.regex.Pattern

class EmailEditText(context: Context, attributeSet: AttributeSet):
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
        context.theme.obtainStyledAttributes(attributeSet, R.styleable.EmailEditText, 0, 0)
            .apply {
                try {
                    isMandatory = getBoolean(R.styleable.EmailEditText_isMandatory, false)

                    background = null
                } finally {
                    recycle()
                }
            }
        inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val pattern = Pattern.compile(RegexPattern.E_MAIL, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(text.toString())
        val isValid = matcher.matches()

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

}