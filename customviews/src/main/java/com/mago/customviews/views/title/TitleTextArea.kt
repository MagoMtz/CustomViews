package com.mago.customviews.views.title

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.util.AttributeSet
import android.view.View
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.MultiAutoCompleteTextView
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
    var isMandatory: Boolean = false
        set(value) {
            field = value
            textArea.isMandatory = value
            invalidate()
            requestLayout()
        }
    var noBackground: Boolean = false
        set(value) {
            field = value
            textArea.noBackground = value
            invalidate()
            requestLayout()
        }
    var title: String = ""
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
                    getString(R.styleable.TitleTextArea_title)?.let { title = it }
                } finally {
                    recycle()
                }
            }
    }

    private fun initComponents() {
        inputLy = findViewById(R.id.input_ly)
        textArea = findViewById(R.id.text_area)

        inputLy.hint = title
        textArea.isMandatory = isMandatory
        textArea.noBackground = noBackground
    }

    fun isValid(): Boolean = textArea.isValid()

    fun setText(resId: Int) {
        textArea.setText(resId)
    }

    fun setText(string: String?) {
        textArea.setText(string)
    }

    fun getText(): Editable? = textArea.text

    fun setAdapter(data: List<String>) {
        textArea.setAdapter(data)
    }

    fun setTokenizer(tokenizer: MultiAutoCompleteTextView.Tokenizer) {
        textArea.setTokenizer(tokenizer)
    }

    fun enableViews(isEnabled: Boolean) {
        inputLy.isEnabled = isEnabled
        textArea.isEnabled = isEnabled
    }

}