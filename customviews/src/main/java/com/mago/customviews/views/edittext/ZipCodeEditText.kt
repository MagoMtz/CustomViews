package com.mago.customviews.views.edittext

import android.content.Context
import android.graphics.Canvas
import android.graphics.RectF
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.text.Editable
import android.text.InputFilter
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
        filters = arrayOf(InputFilter.LengthFilter(5))
    }

    private fun setupAttributes() {
        context.theme.obtainStyledAttributes(attributeSet, R.styleable.ZipCodeEditText, 0, 0)
            .apply {
                try {
                    isMandatory = getBoolean(R.styleable.ZipCodeEditText_isMandatory, false)

                } finally {
                    recycle()
                }
            }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.apply {
            val bg = background as LayerDrawable
            val gradientDrawable = bg.getDrawable(0) as GradientDrawable
            if (isMandatory)
                if (!isValid())
                    gradientDrawable.setStroke(4, ContextCompat.getColor(context, R.color.frame_invalid))
                else
                    gradientDrawable.setStroke(4, ContextCompat.getColor(context, R.color.shadow))
            else
                gradientDrawable.setStroke(4, ContextCompat.getColor(context, R.color.shadow))
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

                if (editable.toString() != "") {
                    val mPattern = Pattern.compile(RegexPattern.ONLY_NUMBERS)
                    val matcher = mPattern.matcher(editable.toString())

                    if (matcher.find()) {
                        val oldSelection = matcher.start()
                        val text = editable.replace(RegexPattern.ONLY_NUMBERS.toRegex(), "")
                        setText(text)
                        setSelection(oldSelection)
                    }
                }
            }
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    }

    fun isValid(): Boolean = text.toString().length == 5

}