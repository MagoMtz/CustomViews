package com.mago.customviews.views.title

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.google.android.material.textfield.TextInputLayout
import com.mago.customviews.R
import com.mago.customviews.views.edittext.EmailEditText

class TitleEmailEditText(context: Context, attributeSet: AttributeSet):
    LinearLayout (context, attributeSet) {

    private lateinit var inputLy: TextInputLayout
    var editText: EmailEditText = EmailEditText(context, attributeSet)
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
    var isMandatory: Boolean = false
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }

    init {
        View.inflate(context, R.layout.title_email_edit_text, this)

        context.theme.obtainStyledAttributes(attributeSet, R.styleable.TitleEmailEditText, 0, 0)
            .apply {
                try {
                    isMandatory = getBoolean(R.styleable.TitleEmailEditText_isMandatory, false)

                    getString(R.styleable.TitleEmailEditText_titleHint)?.let { titleHint = it }
                } finally {
                    recycle()
                }
            }

        initComponents()
    }

    private fun initComponents() {
        inputLy = findViewById(R.id.input_ly)
        editText = findViewById(R.id.edit_text)

        inputLy.hint = titleHint
        editText.isMandatory = isMandatory
    }
}