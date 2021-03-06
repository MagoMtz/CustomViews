package com.mago.customviews.views.title

import android.content.Context
import android.text.Editable
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
class TitleEditText : LinearLayout {
    private lateinit var attributeSet: AttributeSet
    // Attributes
    var onlyNumbers: Boolean = false
        set(value) {
            field = value
            customEditText.onlyNumbers = value
            invalidate()
            requestLayout()
        }
    var charsWithBlankSpaces: Boolean = false
        set(value) {
            field = value
            customEditText.charsWithBlankSpaces = value
            invalidate()
            requestLayout()
        }
    var allChars: Boolean = false
        set(value) {
            field = value
            customEditText.allChars = value
            invalidate()
            requestLayout()
        }
    var noBackground: Boolean = false
        set(value) {
            field = value
            customEditText.noBackground = value
            invalidate()
            requestLayout()
        }
    var isMandatory: Boolean = false
        set(value) {
            field = value
            customEditText.isMandatory = value
            invalidate()
            requestLayout()
        }
    //Views
    private var inputLy: TextInputLayout = TextInputLayout(context)
    var title: String = ""
        //get() = inputLy.hint.toString()
        set(value) {
            inputLy.hint = value
            field = value
            invalidate()
            requestLayout()
        }
    var customEditText: CustomEditText = CustomEditText(context)
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
        View.inflate(context, R.layout.title_edit_text, this)

        initComponents()
    }

    private fun setupAttributes() {
        context.theme.obtainStyledAttributes(attributeSet, R.styleable.TitleEditText, 0, 0)
            .apply {
                try {
                    onlyNumbers =
                        getBoolean(R.styleable.TitleEditText_onlyNumbers, false)
                    charsWithBlankSpaces =
                        getBoolean(R.styleable.TitleEditText_charsWithBlankSpaces, false)
                    allChars =
                        getBoolean(R.styleable.TitleEditText_allChars, false)
                    isMandatory = getBoolean(R.styleable.TitleEditText_isMandatory, false)
                    noBackground = getBoolean(R.styleable.TitleEditText_noBackground, false)

                    getString(R.styleable.TitleEditText_title)?.let { title = it }
                } finally {
                    recycle()
                }
            }
    }

    private fun initComponents() {
        inputLy = findViewById(R.id.input_ly)
        customEditText = findViewById(R.id.custom_edit_text)

        inputLy.hint = title
        customEditText.isMandatory = isMandatory
        customEditText.noBackground = noBackground
        customEditText.onlyNumbers = onlyNumbers
        customEditText.charsWithBlankSpaces = charsWithBlankSpaces
        customEditText.allChars = allChars
    }

    fun isValid(): Boolean = customEditText.isValid()

    fun setText(resId: Int) {
        customEditText.setText(resId)
    }

    fun setText(string: String?) {
        customEditText.setText(string)
    }

    fun getText(): Editable? = customEditText.text

    fun enableViews(isEnabled: Boolean) {
        inputLy.isEnabled = isEnabled
        customEditText.isEnabled = isEnabled
    }

    fun onlyUpperCase(upperCase: Boolean) {
        customEditText.onlyUpperCase(upperCase)
    }

}