package com.mago.customviews.views.title

import android.content.Context
import android.text.Editable
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.google.android.material.textfield.TextInputLayout
import com.mago.customviews.R
import com.mago.customviews.views.edittext.TextArea

/**
 * @author by jmartinez
 * @since 05/02/2020.
 */
class TitleTextArea : LinearLayout{
    private lateinit var attributeSet: AttributeSet
    // Views
    private var inputLy: TextInputLayout = TextInputLayout(context)
    var textArea: TextArea = TextArea(context)
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
        //get() = inputLy.hint.toString()
        set(value) {
            inputLy.hint = value
            field = value
            invalidate()
            requestLayout()
        }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        this.attributeSet = attributeSet
        setupAttributes()
        init()
    }
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int)
            : super(context, attributeSet, defStyleAttr) {
        this.attributeSet = attributeSet
        setupAttributes()
        init()
    }
    constructor(context: Context) : super(context) {
        init()
    }

    private fun init() {
        View.inflate(context, R.layout.title_text_area, this)

        initComponents()
    }

    private fun setupAttributes() {
        context.theme.obtainStyledAttributes(attributeSet, R.styleable.TitleTextArea, 0, 0)
            .apply {
                try {
                    isMandatory = getBoolean(R.styleable.TitleTextArea_isMandatory, false)
                    getString(R.styleable.TitleTextArea_title)?.let { titleHint = it }
                } finally {
                    recycle()
                }
            }
    }

    private fun initComponents() {
        inputLy = findViewById(R.id.input_ly)
        textArea = findViewById(R.id.text_area)

        inputLy.hint = titleHint
        textArea.isMandatory = isMandatory
    }

    fun isValid(): Boolean = textArea.isValid

    fun setText(resId: Int) {
        textArea.setText(resId)
    }

    fun setText(string: String) {
        textArea.setText(string)
    }

    fun getText(): Editable? = textArea.text

    fun invalidateViews() {
        inputLy.isEnabled = false
        textArea.isEnabled = false
    }

    fun validateViews() {
        inputLy.isEnabled = true
        textArea.isEnabled = true
    }



}