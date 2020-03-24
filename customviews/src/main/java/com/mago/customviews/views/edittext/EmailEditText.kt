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

            background = if (isMandatory)
                if (!isValid)
                    ContextCompat.getDrawable(context, R.drawable.bg_common_invalid)
                else
                    ContextCompat.getDrawable(context, R.drawable.bg_common)
            else
                ContextCompat.getDrawable(context, R.drawable.bg_common)
        }

    }

}