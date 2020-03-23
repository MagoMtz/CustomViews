package com.mago.customviews.views.title

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.google.android.material.textfield.TextInputLayout
import com.mago.customviews.R
import com.mago.customviews.views.edittext.CustomEditText

/**
 * @author by jmartinez
 * @since 05/02/2020.
 */
class TitleEditText(context: Context, attributeSet: AttributeSet) :
    LinearLayout(context, attributeSet) {
    //Views
    private lateinit var inputLy: TextInputLayout
    var customEditText: CustomEditText? = null
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }

    // Attributes
    private var titleHint: String = ""
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }
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

    init {
        View.inflate(context, R.layout.title_edit_text, this)

        context.theme.obtainStyledAttributes(attributeSet, R.styleable.TitleEditText, 0, 0)
            .apply {
                try {
                    onlyNumbers =
                        getBoolean(R.styleable.TitleEditText_onlyNumbers, false)
                    charsWithBlankSpaces =
                        getBoolean(R.styleable.TitleEditText_charsWithBlankSpaces, false)
                    isMandatory = getBoolean(R.styleable.TitleEditText_isMandatory, false)

                    getString(R.styleable.TitleEditText_titleHint)?.let { titleHint = it }
                } finally {
                    recycle()
                }
            }

        initComponents()
    }

    private fun initComponents() {
        inputLy = findViewById(R.id.input_ly)
        customEditText = findViewById(R.id.custom_edit_text)

        inputLy.hint = titleHint
        customEditText?.isMandatory = isMandatory
        customEditText?.onlyNumbers = onlyNumbers
        customEditText?.charsWithBlankSpaces = charsWithBlankSpaces
    }
}