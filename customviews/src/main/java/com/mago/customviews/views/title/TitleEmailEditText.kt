package com.mago.customviews.views.title

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.google.android.material.textfield.TextInputLayout
import com.mago.customviews.R
import com.mago.customviews.views.edittext.EmailEditText

class TitleEmailEditText: LinearLayout {
    private lateinit var attributeSet: AttributeSet
    // Views
    private var inputLy: TextInputLayout = TextInputLayout(context)
    var titleHint: String = ""
        //get() = inputLy.hint.toString()
        set(value) {
            inputLy.hint = value
            field = value
            invalidate()
            requestLayout()
        }
    var editText: EmailEditText = EmailEditText(context)
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }
    // Attr
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
        View.inflate(context, R.layout.title_email_edit_text, this)

        initComponents()
    }

    private fun setupAttributes() {
        context.theme.obtainStyledAttributes(attributeSet, R.styleable.TitleEmailEditText, 0, 0)
            .apply {
                try {
                    isMandatory = getBoolean(R.styleable.TitleEmailEditText_isMandatory, false)

                    getString(R.styleable.TitleEmailEditText_titleHint)?.let {
                        titleHint = it
                    }
                } finally {
                    recycle()
                }
            }

    }

    private fun initComponents() {
        inputLy = findViewById(R.id.input_ly)
        editText = findViewById(R.id.edit_text)

        inputLy.hint = titleHint
        editText.isMandatory = isMandatory
    }
}