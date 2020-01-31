package com.mago.customviews.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.core.graphics.toRectF
import com.mago.customviews.R
import com.mago.customviews.util.Regex
import java.util.regex.Pattern

/**
 * @author by jmartinez
 * @since 15/01/2020.
 */
class BiographicEditText(context: Context, attributeSet: AttributeSet) :
    EditText(context, attributeSet) {
    // Attributes
    private var onlyNumbers: Boolean = false
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }
    private var charsWithBlankSpaces: Boolean = false
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }
    private var isMandatory: Boolean = false
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }

    // Paint objects
    private val framePaint = Paint(ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.dark_gray)
        style = Paint.Style.STROKE
        strokeWidth = 3F
    }

    private val frameAlertPaint = Paint(ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.frame_invalid)
        style = Paint.Style.STROKE
        strokeWidth = 3F
    }

    private val hintTextPaint = Paint(ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.colorPrimary)
        textSize = 38F
    }

    init {
        context.theme.obtainStyledAttributes(attributeSet, R.styleable.BiographicEditText, 0, 0)
            .apply {
                try {
                    onlyNumbers =
                        getBoolean(R.styleable.BiographicEditText_onlyNumbers, false)
                    charsWithBlankSpaces =
                        getBoolean(R.styleable.BiographicEditText_charsWithBlankSpaces, false)
                    isMandatory = getBoolean(R.styleable.BiographicEditText_isMandatory, false)


                    background = null
                } finally {
                    recycle()
                }
            }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val textIsEmpty = text.toString().isEmpty()

        canvas?.apply {

            val cBounds = clipBounds
            cBounds.inset(0, 35)

            if (isMandatory)
                if (textIsEmpty)
                    drawRoundRect(cBounds.toRectF(), 10F, 10F, frameAlertPaint)
                else
                    drawRoundRect(cBounds.toRectF(), 10F, 10F, framePaint)
            else
                drawRoundRect(cBounds.toRectF(), 10F, 10F, framePaint)

            if (!textIsEmpty)
                drawText(hint.toString(), 0F, 27F, hintTextPaint)

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

                // Pattern for only text
                val pattern = when {
                    onlyNumbers -> Regex.ONLY_NUMBERS
                    charsWithBlankSpaces -> Regex.A_TO_Z_WITH_BLANK_SPACES
                    else -> Regex.A_TO_Z
                }
                val mPattern = Pattern.compile(pattern)

                if (editable.toString() != "") {
                    val matcher = mPattern.matcher(editable[count - 1].toString())

                    if (!matcher.matches()) {
                        val text = s.subSequence(0, count - 1)
                        setText(text)
                        setSelection(count - 1)
                    }
                }
            }
        }

        override fun beforeTextChanged(
            s: CharSequence?,
            start: Int,
            count: Int,
            after: Int
        ) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            invalidate()
        }
    }

}