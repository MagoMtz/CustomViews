package com.mago.customviews.views.edittext

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.mago.customviews.R
import com.mago.customviews.util.RegexPattern
import java.util.regex.Pattern

class ZipCodeEditText : AppCompatEditText{
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
    }

    private fun setupAttributes() {
        context.theme.obtainStyledAttributes(attributeSet, R.styleable.ZipCodeEditText, 0, 0)
            .apply {
                try {
                    isMandatory = getBoolean(R.styleable.ZipCodeEditText_isMandatory, false)

                    background = null
                } finally {
                    recycle()
                }
            }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.apply {
            val cBounds = clipBounds
            cBounds.inset(0, 8)

            background = if (isMandatory)
                if (!isValid())
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
        override fun afterTextChanged(s: Editable?) {
            s?.let { editable ->
                val count = editable.length
                val mPattern = Pattern.compile(RegexPattern.ONLY_NUMBERS)

                setSelection(count)

                if (editable.toString() != "") {
                    val matcher = mPattern.matcher(editable[count -1].toString())

                    if (!matcher.matches() || count > 5) {
                        val text = s.subSequence(0, count - 1)
                        setText(text)
                    }
                }
            }
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    }

    fun isValid(): Boolean = text.toString().length == 5

}