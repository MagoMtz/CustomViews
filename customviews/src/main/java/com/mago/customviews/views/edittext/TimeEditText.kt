package com.mago.customviews.views.edittext

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.mago.customviews.util.Constants
import com.mago.customviews.util.Constants.REPLACE_TIME_FORMAT
import com.mago.customviews.util.Constants.TIME_FORMAT
import com.mago.customviews.util.RegexPattern
import com.mago.customviews.util.RegexPattern.DATE_TIME_PLACEHOLDER

/**
 * @author by jmartinez
 * @since 11/06/2020.
 */
class TimeEditText: AppCompatEditText {
    private lateinit var attributeSet: AttributeSet

    private var current = ""
    private var hhmm = "HHMM"

    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet) {
        this.attributeSet = attributeSet
        init()
    }
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int):
            super(context, attributeSet, defStyleAttr) {
        this.attributeSet = attributeSet
        init()
    }
    constructor(context: Context): super(context) {
        init()
    }

    private fun init() {
        inputType = InputType.TYPE_CLASS_NUMBER

        background = null
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
            val data = s.toString()

            if (data != current) {
                if (data.replace(DATE_TIME_PLACEHOLDER.toRegex(), "").length == 4) {
                    requestFocus()
                    setSelection(0)
                }

                var clean = data.replace(DATE_TIME_PLACEHOLDER.toRegex(), "")
                var cleanC = current.replace(DATE_TIME_PLACEHOLDER.toRegex(), "")

                val cl = clean.length
                var sel = cl

                var index = 2

                while (index <= cl && index < 4) {
                    index += 2
                    sel++
                }

                if (clean == cleanC) sel--

                if (clean.length < 4) {
                    clean += hhmm.substring(clean.length)
                } else {
                    var hours = Integer.parseInt(clean.substring(0, 2))
                    var minutes = Integer.parseInt(clean.substring(2, 4))

                    hours = when {
                        hours < 0 -> 0
                        hours > 24 -> 24
                        else -> hours
                    }
                    minutes = when {
                        minutes < 0 -> 0
                        minutes > 60 -> 60
                        else -> minutes
                    }

                    clean = String.format(TIME_FORMAT, hours,minutes)
                }

                clean = String.format(
                    REPLACE_TIME_FORMAT,
                    clean.substring(0, 2),
                    clean.substring(2, 4)
                )

                sel = if (sel < 0) 0 else sel

                current = clean

                setText(current)
                setSelection(if (sel < current.length) sel else current.length)
                requestLayout()
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }


}