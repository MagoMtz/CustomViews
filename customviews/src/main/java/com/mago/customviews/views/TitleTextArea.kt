package com.mago.customviews.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.google.android.material.textfield.TextInputLayout
import com.mago.customviews.R

/**
 * @author by jmartinez
 * @since 05/02/2020.
 */
class TitleTextArea(context: Context, attributeSet: AttributeSet) :
    LinearLayout(context, attributeSet) {
    // Views
    private lateinit var inputLy: TextInputLayout
    var textArea: TextArea? = null
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }

    // Attributes
    private var isMandatory: Boolean = false
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }
    private var titleHint: String = ""
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }

    init {
        View.inflate(context, R.layout.title_text_area, this)

        context.theme.obtainStyledAttributes(attributeSet, R.styleable.TitleTextArea, 0, 0)
            .apply {
                try {
                    isMandatory = getBoolean(R.styleable.TitleTextArea_isMandatory, false)
                    getString(R.styleable.TitleTextArea_titleHint)?.let { titleHint = it }
                } finally {
                    recycle()
                }
            }
        initComponents()
    }

    private fun initComponents() {
        inputLy = findViewById(R.id.input_ly)
        textArea = findViewById(R.id.text_area)

        inputLy.hint = titleHint
        textArea?.isMandatory = isMandatory
    }
}