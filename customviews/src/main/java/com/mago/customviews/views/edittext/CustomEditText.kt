package com.mago.customviews.views.edittext

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.graphics.toRectF
import androidx.core.view.marginTop
import com.mago.customviews.R
import com.mago.customviews.util.CommonUtils
import com.mago.customviews.util.RegexPattern
import java.util.regex.Pattern

/**
 * @author by jmartinez
 * @since 15/01/2020.
 */
open class CustomEditText :
    AppCompatEditText {
    private lateinit var attributeSet: AttributeSet

    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet) {
        this.attributeSet = attributeSet
        init()
    }
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int):
            super(context, attributeSet, defStyleAttr) {
        this.attributeSet = attributeSet
        init()
    }
    constructor(context: Context): super(context)

    // Attributes
    var onlyNumbers: Boolean = false
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }
    var charsWithBlankSpaces: Boolean = false
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }
    var allChars: Boolean = false
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

    // Paint objects
    private val framePaint = Paint(ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.border)
        style = Paint.Style.STROKE
        strokeWidth = 3F
    }

    private val frameAlertPaint = Paint(ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.frame_invalid)
        style = Paint.Style.STROKE
        strokeWidth = 3F
    }

    fun init() {
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
        inputType = if (onlyNumbers)
            InputType.TYPE_CLASS_NUMBER
        else
            InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
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
        setPadding(
            CommonUtils.intToDp(context, 8),
            CommonUtils.intToDp(context, 7)
            ,0
            ,0)
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
                    onlyNumbers -> RegexPattern.ONLY_NUMBERS
                    charsWithBlankSpaces -> RegexPattern.A_TO_Z_WITH_BLANK_SPACES
                    allChars -> RegexPattern.ALL_CHARS
                    else -> RegexPattern.A_TO_Z
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
        ) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            invalidate()
        }
    }

}