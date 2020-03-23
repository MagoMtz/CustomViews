package com.mago.customviews.views.edittext

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.graphics.toRectF
import androidx.core.text.trimmedLength
import com.mago.customviews.R
import com.mago.customviews.util.RegexPattern
import org.w3c.dom.Text
import java.util.regex.Pattern

class PhoneEditText(context: Context, attributeSet: AttributeSet):
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
        context.theme.obtainStyledAttributes(attributeSet, R.styleable.PhoneEditText, 0, 0)
            .apply {
                try {
                    isMandatory = getBoolean(R.styleable.PhoneEditText_isMandatory, false)

                    background = null
                } finally {
                    recycle()
                }
            }
        inputType = InputType.TYPE_CLASS_PHONE
        filters = arrayOf(InputFilter.LengthFilter(14))
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val isValid = text.toString().length == 14

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
        private var backspacing = false
        private var edited = false
        private var cursorCompleted = 0

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            cursorCompleted = s.length - selectionStart

            backspacing = count > after
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(s: Editable) {
            val string: String = s.toString()
            val phone = string.replace("[^\\d]".toRegex(), "")

            if (!edited) {
                if (phone.length >= 6 && !backspacing) {
                    edited = true
                    val ans = "(${phone.substring(0,3)}) ${phone.substring(3,6)}-${phone.substring(6)}"
                    setText(ans)
                    setSelection(text!!.length-cursorCompleted)
                } else if (phone.length >= 3 && !backspacing) {
                    edited = true
                    val ans = "(${phone.substring(0,3)}) ${phone.substring(3)}"
                    setText(ans)
                    setSelection(text!!.length-cursorCompleted)
                }
            } else {
                edited = false
            }
        }

    }

    fun getPhoneNumber(): String {
        return text!!.replace("[^\\d]".toRegex(), "")
    }

}