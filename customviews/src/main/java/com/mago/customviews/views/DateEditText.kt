package com.mago.customviews.views

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.widget.EditText
import com.mago.customviews.util.Constants.DATE_FORMAT
import com.mago.customviews.util.Constants.REPLACE_DATE_FORMAT
import com.mago.customviews.util.Regex.DATE
import java.util.*

/**
 * @author by jmartinez
 * @since 06/02/2020.
 */
class DateEditText(context: Context, attributeSet: AttributeSet) : EditText(context, attributeSet) {

    private var current = ""
    private val ddmmyyyy = "DDMMYYYY"
    private val cal = Calendar.getInstance()

    init {
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
            if (data != current) {
                if (data.replace(DATE.toRegex(), "").length == 8) {
                    requestFocus()
                    setSelection(0)
                }

                var clean = data.replace(DATE.toRegex(), "")
                val cleanC = current.replace(DATE.toRegex(), "")

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
                setText(current)
                setSelection(if (sel < current.length) sel else current.length)
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

}