package com.mago.customviews.views.edittext

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatEditText
import com.mago.customviews.util.Constants.DATE_FORMAT
import com.mago.customviews.util.Constants.DATE_PLACE_HOLDER
import com.mago.customviews.util.Constants.REPLACE_DATE_FORMAT
import com.mago.customviews.util.RegexPattern.DATE_TIME_PLACEHOLDER
import java.util.*

/**
 * @author by jmartinez
 * @since 06/02/2020.
 */
class DateEditText: AppCompatEditText {
    private lateinit var attributeSet: AttributeSet

    private var current = ""
    private val ddmmyyyy = "DDMMYYYY"
    private val cal = Calendar.getInstance()

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
        inputType = InputType.TYPE_CLASS_DATETIME

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
            if (data == "")
                return

            if (data != current) {
                if (data.replace(DATE_TIME_PLACEHOLDER.toRegex(), "").length == 8) {
                    requestFocus()
                    setSelection(0)
                }

                var clean = data.replace(DATE_TIME_PLACEHOLDER.toRegex(), "")
                val cleanC = current.replace(DATE_TIME_PLACEHOLDER.toRegex(), "")

                val cl = clean.length
                var sel = cl

                var index = 2

                while (index <= cl && index < 6) {
                    index += 2
                    sel++
                }

                if (clean == cleanC) sel--

                if (clean.length < 8) {
                    clean += ddmmyyyy.substring(clean.length)
                } else {
                    var day = Integer.parseInt(clean.substring(0, 2))
                    var mon = Integer.parseInt(clean.substring(2, 4))
                    var year = Integer.parseInt(clean.substring(4, 8))

                    val calendar = Calendar.getInstance()
                    val minYear = calendar.get(Calendar.YEAR) - 100
                    val maxYear = calendar.get(Calendar.YEAR)

                    mon = if (mon < 1) 1 else if (mon > 12) 12 else mon
                    cal.set(Calendar.MONTH, mon - 1)
                    year =
                        if (year < minYear) minYear else if (year > maxYear) maxYear else year
                    cal.set(Calendar.YEAR, year)

                    day =
                        if (day > cal.getActualMaximum(Calendar.DATE)) cal.getActualMaximum(
                            Calendar.DATE
                        ) else day
                    clean = String.format(DATE_FORMAT, day, mon, year)
                }

                clean = String.format(
                    REPLACE_DATE_FORMAT, clean.substring(0, 2),
                    clean.substring(2, 4),
                    clean.substring(4, 8)
                )

                sel = if (sel < 0) 0 else sel
                current = clean
                if (current == DATE_PLACE_HOLDER)
                    setText("")
                else
                    setText(current)
                setSelection(if (sel < current.length) sel else current.length)
                requestLayout()
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

}