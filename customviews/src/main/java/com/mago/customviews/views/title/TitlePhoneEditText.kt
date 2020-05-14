package com.mago.customviews.views.title

import android.content.Context
import android.text.Editable
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.google.android.material.textfield.TextInputLayout
import com.mago.customviews.R
import com.mago.customviews.views.edittext.PhoneEditText

class TitlePhoneEditText : LinearLayout {
    private lateinit var attributeSet: AttributeSet
    // Views
    private var inputLy: TextInputLayout = TextInputLayout(context)
    var title: String = ""
        set(value) {
            inputLy.hint = value
            field = value
            invalidate()
            requestLayout()
        }
    var editText: PhoneEditText = PhoneEditText(context)
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }
    // Attr
    var isMandatory: Boolean = false
        set(value) {
            field = value
            editText.isMandatory
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
        View.inflate(context, R.layout.title_phone_edit_text, this)

        initComponents()
    }

    private fun setupAttributes() {
        context.theme.obtainStyledAttributes(attributeSet, R.styleable.TitlePhoneEditText, 0, 0)
            .apply {
                try {
                    isMandatory = getBoolean(R.styleable.TitlePhoneEditText_isMandatory, false)

                    getString(R.styleable.TitlePhoneEditText_title)?.let { title = it }
                } finally {
                    recycle()
                }
            }
    }

    private fun initComponents() {
        inputLy = findViewById(R.id.input_ly)
        editText = findViewById(R.id.edit_text)

        inputLy.hint = title
        editText.isMandatory = isMandatory
    }

    fun isValid(): Boolean = editText.isValid

    fun setText(resId: Int) {
        editText.setText(resId)
    }

    fun setText(string: String?) {
        editText.setText(string)
    }

    fun getPhoneNumber() = editText.getPhoneNumber()

    fun enableViews(isEnabled: Boolean) {
        inputLy.isEnabled = isEnabled
        editText.isEnabled = isEnabled
    }

}