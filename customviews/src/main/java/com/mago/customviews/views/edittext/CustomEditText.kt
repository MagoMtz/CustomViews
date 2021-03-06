package com.mago.customviews.views.edittext

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
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
            if (value) {
                charsWithBlankSpaces = false
                allChars = false
            }
            setupInputType()
            invalidate()
            requestLayout()
        }
    var charsWithBlankSpaces: Boolean = false
        set(value) {
            field = value
            if (value) {
                onlyNumbers = false
                allChars = false
            }
            setupInputType()
            invalidate()
            requestLayout()
        }
    var allChars: Boolean = false
        set(value) {
            field = value
            if (value) {
                onlyNumbers = false
                charsWithBlankSpaces = false
            }
            setupInputType()
            invalidate()
            requestLayout()
        }
    var noBackground: Boolean = false
        set(value) {
            field = value
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
                    noBackground = getBoolean(R.styleable.CustomEditText_noBackground, false)
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

        canvas?.apply {
            val cBounds = clipBounds
            cBounds.inset(0, 8)

            if (noBackground) {
                background = null
                return@apply
            }

            background =
                if (isMandatory) {
                    if (!isValid()) {
                        ContextCompat.getDrawable(context, R.drawable.bg_common_invalid)
                    } else {
                        ContextCompat.getDrawable(context, R.drawable.bg_common)
                    }
                } else {
                    ContextCompat.getDrawable(context, R.drawable.bg_common)
                }

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
            if (allChars)
                return

            s?.let { editable ->
                // Pattern for only text
                val pattern = when {
                    onlyNumbers -> RegexPattern.ONLY_NUMBERS
                    charsWithBlankSpaces -> RegexPattern.A_TO_Z_WITH_BLANK_SPACES
                    allChars -> RegexPattern.ALL_CHARS
                    else -> RegexPattern.A_TO_Z
                }

                if (editable.toString() != "") {
                    val mPattern = Pattern.compile(pattern)
                    val matcher = mPattern.matcher(editable.toString())

                    if (matcher.find()) {
                        val oldSelection = matcher.start()
                        val mText = editable.replace(pattern.toRegex(), "")
                        setText(mText)
                        setSelection(oldSelection)
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