package com.mago.customviews.views.edittext

import android.content.Context
import android.graphics.Canvas
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.util.AttributeSet
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatMultiAutoCompleteTextView
import androidx.core.content.ContextCompat
import com.mago.customviews.R

/**
 * @author by jmartinez
 * @since 05/02/2020.
 */
class TextArea : AppCompatMultiAutoCompleteTextView {
    private lateinit var attributeSet: AttributeSet
    // Attributes
    var isMandatory: Boolean = false
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }
    var isValid = false

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
        minLines = 1
        setLines(3)
        maxLines = 15
    }

    private fun setupAttributes() {
        context.theme.obtainStyledAttributes(attributeSet, R.styleable.TextArea, 0, 0)
            .apply {
                try {
                    isMandatory = getBoolean(R.styleable.TextArea_isMandatory, false)
                } finally {
                    recycle()
                }
            }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val isNotValid = text.toString().isEmpty()
        isValid = !isNotValid

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
    }

    fun setAdapter(data: List<String>) {
        val adapter = ArrayAdapter<String>(
            context,
            android.R.layout.simple_dropdown_item_1line,
            data
        )

        val tokenizer = object : Tokenizer {
            override fun findTokenStart(text: CharSequence, cursor: Int): Int {
                var i = cursor

                while (i > 0 && text[i -1] != ' ') {
                    i--
                }
                while (i < cursor && text[i] == ' ') {
                    i++
                }

                return i
            }

            override fun findTokenEnd(text: CharSequence, cursor: Int): Int {
                var i = cursor
                val len = text.length

                while (i < len) {
                    if (text[i] == ' '){
                        return  i
                    } else {
                        i++
                    }
                }

                return len
            }


            override fun terminateToken(text: CharSequence): CharSequence {
                var i = text.length

                while (i > 0 && text[i-1] == ' ') {
                    i--
                }

                return if (i > 0 && text[i-1] == ' ') {
                    text
                } else {
                    if (text is Spanned) {
                        val sp = SpannableString("$text ")
                        TextUtils.copySpansFrom(text, 0, text.length, Any::class.java, sp, 0)
                        sp
                    } else {
                        "$text "
                    }
                }
            }
        }

        setAdapter(adapter)
        setTokenizer(tokenizer)
        adapter.notifyDataSetChanged()
    }

}