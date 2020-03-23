package com.mago.customviews.views.edittext

import android.app.DatePickerDialog
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.mago.customviews.R
import com.mago.customviews.util.CommonUtils
import com.mago.customviews.util.RegexPattern.DATE
import java.util.*

class DateRange(context: Context, attributeSet: AttributeSet) :
    LinearLayout(context, attributeSet) {

    private lateinit var initialDatePicker: DatePickerDialog
    private lateinit var finalDatePicker: DatePickerDialog

    // Views
    var initDate: SingleDate? = null
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }
    var finalDate: SingleDate? = null
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }

    // Attributes
    var isMandatory: Boolean = false
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }
    var initDateTitle: String = ""
        set(value) {
            field = value
            initDate?.titleHint = value
            invalidate()
            requestLayout()
        }
    var finalDateTitle: String = ""
        set(value) {
            field = value
            finalDate?.titleHint = value
            invalidate()
            requestLayout()
        }
    var daysRange: Int = 0
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }
    var futureDate: Boolean = false
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }

    init {
        View.inflate(context, R.layout.date_range, this)

        context.theme.obtainStyledAttributes(attributeSet, R.styleable.DateRange, 0, 0)
            .apply {
                try {
                    isMandatory = getBoolean(R.styleable.DateRange_isMandatory, false)
                    getString(R.styleable.DateRange_initDateTitle)?.let {
                        initDateTitle = it
                    }
                    getString(R.styleable.DateRange_finalDateTitle)?.let {
                        finalDateTitle = it
                    }
                    daysRange = getInteger(R.styleable.DateRange_daysRange, 30) - 1
                    futureDate = getBoolean(R.styleable.SingleDate_futureDate, false)
                } finally {
                    recycle()
                }
            }

        initComponents()
        setupDateRangePicker()
    }

    private fun initComponents() {
        initDate = findViewById(R.id.init_date)
        finalDate = findViewById(R.id.final_date)

        initDate?.let {
            it.titleHint = initDateTitle
            it.isMandatory = isMandatory
        }
        finalDate?.let {
            it.titleHint = finalDateTitle
            it.isMandatory = isMandatory
        }
    }

    private fun setupInitDatePicker() {
        val calendar = Calendar.getInstance()
        var day = calendar.get(Calendar.DAY_OF_MONTH)
        var month = calendar.get(Calendar.MONTH)
        var year = calendar.get(Calendar.YEAR)

        val initDateText = initDate?.dateEditText?.text.toString()
        val dateIsComplete = initDateText.replace(DATE.toRegex(), "").length == 8

        if (initDateText.isNotEmpty() && dateIsComplete) {
            val date = initDate?.dateEditText?.text.toString().split("/")
            day = date[0].toInt()
            month = date[1].toInt() - 1
            year = date[2].toInt()
        }
        initialDatePicker = DatePickerDialog(
            context!!,
            DatePickerDialog.OnDateSetListener { _, mYear, monthOfYear, dayOfMonth ->
                val initialDate =
                    (if (dayOfMonth.toString().length < 2) "0".plus(dayOfMonth.toString()) else dayOfMonth.toString()) + "/" +
                            (if ((monthOfYear + 1).toString().length < 2) "0" + (monthOfYear + 1).toString() else (monthOfYear + 1).toString()) + "/" + mYear
                initDate?.dateEditText?.setText(initialDate)
                finalDate?.dateEditText?.setText(initialDate)
                setupFinalDatePicker()
                finalDatePicker.show()

            }, year, month, day
        )
        if (!futureDate)
            initialDatePicker.datePicker.maxDate = Date().time
    }

    private fun setupFinalDatePicker() {
        val calendar = Calendar.getInstance()
        var day = calendar.get(Calendar.DAY_OF_MONTH)
        var month = calendar.get(Calendar.MONTH)
        var year = calendar.get(Calendar.YEAR)

        val finalDateText = finalDate?.dateEditText?.text.toString()
        val initDateText = initDate?.dateEditText?.text.toString()
        val initDateIsComplete = initDateText.replace(DATE.toRegex(), "").length == 8

        if (finalDateText.isNotEmpty() && initDateIsComplete) {
            val date = initDateText.split("/")
            day = date[0].toInt()
            month = date[1].toInt()
            year = date[2].toInt()
        }
        finalDatePicker = DatePickerDialog(
            context!!,
            DatePickerDialog.OnDateSetListener { _, mYear, monthOfYear, dayOfMonth ->
                val finalDate =
                    (if (dayOfMonth.toString().length < 2) "0".plus(dayOfMonth.toString()) else dayOfMonth.toString()) + "/" +
                            (if ((monthOfYear + 1).toString().length < 2) "0" + (monthOfYear + 1).toString() else (monthOfYear + 1).toString()) + "/" + mYear
                this.finalDate?.dateEditText?.setText(finalDate)

            }, year, month, day
        )

        val initialDate: Date
        if (initDateText.isNotEmpty() && initDateIsComplete) {
            initialDate = CommonUtils.getDate(initDateText)
            finalDatePicker.datePicker.minDate = initialDate.time
        } else {
            initialDate = Date()
        }

        if (CommonUtils.getDaysDiff(initialDate, Date()) > daysRange) {
            if (futureDate) {
                finalDatePicker.datePicker.maxDate = CommonUtils.getDaysSum(initialDate, daysRange)
            } else {
                finalDatePicker.datePicker.maxDate = Date().time
            }
        } else {
            val c = Calendar.getInstance()
            c.time = initialDate
            c.add(Calendar.DATE, daysRange)
            val maxDate = c.time
            if (maxDate > Date()) {
                if (futureDate) {
                    finalDatePicker.datePicker.maxDate =
                        CommonUtils.getDaysSum(initialDate, daysRange)
                } else {
                    finalDatePicker.datePicker.maxDate = Date().time
                }
            } else {
                finalDatePicker.datePicker.maxDate = maxDate.time
            }
        }
    }

    private fun setupDateRangePicker() {
        initDate?.btnCalendar?.setOnClickListener {
            setupInitDatePicker()
            initialDatePicker.show()
        }

        finalDate?.btnCalendar?.setOnClickListener {
            setupFinalDatePicker()
            finalDatePicker.show()
        }
    }

}