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
import com.mago.customviews.util.CommonUtils
import com.mago.customviews.util.RegexPattern
import java.util.*
import java.util.regex.Pattern

/**
 * @author by jmartinez
 * @since 15/01/2020.
 */
open class CustomEditText : AppCompatEditText {
    private lateinit var attributeSet: AttributeSet
    // Attributes
    var onlyNumbers: Boolean = false
        set(value) {
            field = value
            setupInputType()
            invalidate()
            requestLayout()
        }
    var charsWithBlankSpaces: Boolean = false
        set(value) {
            field = value
            setupInputType()
            invalidate()
            requestLayout()
        }
    var allChars: Boolean = false
        set(value) {
            field = value
            setupInputType()
            invalidate()
            requestLayout()
        }
    var isMandatory: Boolean = false
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        this.attributeSet = attributeSet
        setupAttributes()
        init()
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) :
            super(context, attributeSet, defStyleAttr) {
        this.attributeSet = attributeSet
        setupAttributes()
        init()
    }

    constructor(context: Context) : super(context) {
        init()
    }


    private fun init() {
        setupInputType()
    }

    private fun setupAttributes() {
        context.theme.obtainStyledAttributes(attributeSet, R.styleable.CustomEditText, 0, 0)
            .apply {
                try {
                    onlyNumbers =
                        getBoolean(R.styleable.CustomEditText_onlyNumbers, false)
                    charsWithBlankSpaces =
                        getBoolean(R.styleable.CustomEditText_charsWithBlankSpaces, false)
                    allChars =
                        getBoolean(R.styleable.CustomEditText_allChars, false)
                    isMandatory = getBoolean(R.styleable.CustomEditText_isMandatory, false)
                } finally {
                    recycle()
                }
            }
    }

    private fun setupInputType() {
        inputType = when {
            onlyNumbers -> InputType.TYPE_CLASS_NUMBER
            else -> InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val isNotValid = text.toString().isEmpty()

        canvas?.apply {
            val cBounds = clipBounds
            cBounds.inset(0, 8)

            background = if (isMandatory)
                if (isNotValid)
                    ContextCompat.getDrawable(context, R.drawable.bg_common_invalid)
                else
                    ContextCompat.getDrawable(context, R.drawable.bg_common)
            else
                ContextCompat.getDrawable(context, R.drawable.bg_common)

        }
        setPadding(
            CommonUtils.intToDp(context, 8),
            CommonUtils.intToDp(context, 7)
            , 0
            , 0
        )
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
                setSelection(count)

                // Pattern for only text
                val pattern = when {
                    onlyNumbers -> RegexPattern.ONLY_NUMBERS
                    charsWithBlankSpaces -> RegexPattern.A_TO_Z_WITH_BLANK_SPACES
                    allChars -> RegexPattern.ALL_CHARS
                    else -> RegexPattern.A_TO_Z
                }
                val mPattern = Pattern.compile(pattern)

                if (editable.toString() != "") {
                    val matcher = mPattern.matcher(editable[count - 1].toString())

                    if (!matcher.matches()) {
                        val mText = editable.subSequence(0, count - 1)
                        this@CustomEditText.setText(mText)
                    }
                }
            }
        }

        override fun beforeTextChanged(
            s: CharSequence?,
            start: Int,
            count: Int,
            after: Int
        ) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            invalidate()
        }
    }

    fun isValid(): Boolean {
        return text.toString().isNotEmpty()
    }

    fun onlyUpperCase(upperCase: Boolean) {
        if (upperCase) {
            inputType = InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
            filters = arrayOf(InputFilter.AllCaps())
        } else {
            inputType = InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
            filters = arrayOf()
        }
    }

}