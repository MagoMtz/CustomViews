package com.mago.customviews.views.edittext

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.mago.customviews.R
import com.mago.customviews.util.RegexPattern
import java.util.regex.Pattern

class EmailEditText : TextInputEditText {
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
        inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
    }

    private fun setupAttributes(){
        context.theme.obtainStyledAttributes(attributeSet, R.styleable.EmailEditText, 0, 0)
            .apply {
                try {
                    isMandatory = getBoolean(R.styleable.EmailEditText_isMandatory, false)

                    //background = null
                } finally {
                    recycle()
                }
            }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val isValid = textMatch()

        canvas?.apply {
            val cBounds = clipBounds
            cBounds.inset(0, 8)

            val bg = background as LayerDrawable
            val gradientDrawable = bg.getDrawable(0) as GradientDrawable
            if (isMandatory)
                if (!isValid)
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
                val count = editable.length
                val mPattern = Pattern.compile(RegexPattern.VALID_ENTER_E_MAIL)

                setSelection(count)

                if (editable.toString() != "") {
                    val matcher = mPattern.matcher(editable[count -1].toString())

                    if (!matcher.matches()) {
                        val text = s.subSequence(0, count - 1)
                        setText(text)
                    }
                }
            }
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    }

    private fun textMatch(): Boolean {
        val pattern = Pattern.compile(RegexPattern.E_MAIL, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(text.toString())
        return matcher.matches()
    }

    fun isValid(): Boolean = textMatch()

}