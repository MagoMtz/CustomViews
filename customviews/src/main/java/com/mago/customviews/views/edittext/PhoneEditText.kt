package com.mago.customviews.views.edittext

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.mago.customviews.R

class PhoneEditText : AppCompatEditText {
    private lateinit var attributeSet: AttributeSet

    var isMandatory: Boolean = false
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }

    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet) {
        this.attributeSet = attributeSet
        setupAttributes()
        init()
    }
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int):
            super(context, attributeSet, defStyleAttr) {
        this.attributeSet = attributeSet
        setupAttributes()
        init()
    }
    constructor(context: Context): super(context) {
        init()
    }

    private fun init() {
        inputType = InputType.TYPE_CLASS_PHONE
        filters = arrayOf(InputFilter.LengthFilter(14))
    }

    private fun setupAttributes() {
        context.theme.obtainStyledAttributes(attributeSet, R.styleable.PhoneEditText, 0, 0)
            .apply {
                try {
                    isMandatory = getBoolean(R.styleable.PhoneEditText_isMandatory, false)

                    background = null
                } finally {
                    recycle()
                }
            }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val isValid = text.toString().length == 14

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