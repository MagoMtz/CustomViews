package com.mago.customviews.views.edittext

import android.app.TimePickerDialog
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.mago.customviews.R
import com.mago.customviews.util.RegexPattern.DATE_TIME_PLACEHOLDER

/**
 * @author by jmartinez
 * @since 11/06/2020.
 */
class TimeRange: LinearLayout {
    private lateinit var attributeSet: AttributeSet
    private lateinit var initialTimePicker: TimePickerDialog
    private lateinit var finalTimePicker: TimePickerDialog

    // Views
    var initTime: SingleTime = SingleTime(context)
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }
    var finalTime: SingleTime = SingleTime(context)
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
    var initTimeTitle: String = ""
        set(value) {
            field = value
            initTime.title = value
            invalidate()
            requestLayout()
        }
    var finalTimeTitle: String = ""
        set(value) {
            field = value
            finalTime.title = value
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

    private fun setupAttributes() {
        context.theme.obtainStyledAttributes(attributeSet, R.styleable.TimeRange, 0, 0)
            .apply {
                try {
                    isMandatory = getBoolean(R.styleable.TimeRange_isMandatory, false)
                    getString(R.styleable.TimeRange_initTimeTitle)?.let {
                        initTimeTitle = it
                    }
                    getString(R.styleable.TimeRange_finalTimeTitle)?.let {
                        finalTimeTitle = it
                    }
                } finally {
                    recycle()
                }
            }
    }

    private fun init() {
        View.inflate(context, R.layout.time_range, this)

        initComponents()
        setupTimeRangePicker()
    }

    private fun initComponents() {
        initTime = findViewById(R.id.init_time)
        finalTime = findViewById(R.id.final_time)

        initTime.let {
            it.title = initTimeTitle
            it.isMandatory = isMandatory
        }
        finalTime.let {
            it.title = finalTimeTitle
            it.isMandatory = isMandatory
        }
    }

    private fun setupTimeRangePicker() {
        initTime.btnTime.setOnClickListener {
            setupInitTimePicker()
            initialTimePicker.show()
        }
        finalTime.btnTime.setOnClickListener {
            setupFinalTimePicker()
            finalTimePicker.show()
        }
    }

    private fun setupInitTimePicker() {
        var hour = 12
        var minutes = 0

        val initTimeText = initTime.timeEditText.text.toString()
        val timeIsComplete = initTimeText.replace(DATE_TIME_PLACEHOLDER.toRegex(), "").length == 4

        if (initTimeText.isNotEmpty() && timeIsComplete) {
            val time = initTime.timeEditText.text.toString().split(":")
            hour = time[0].toInt()
            minutes = time[1].toInt()
        }

        initialTimePicker = TimePickerDialog(
            context,
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                val mHours: String = if (hourOfDay < 10 )
                    "0$hourOfDay"
                else
                    hourOfDay.toString()

                val mMinutes: String = if (minute < 10)
                    "0$minute"
                else
                    minute.toString()

                val initialTime = "$mHours:$mMinutes"

                initTime.timeEditText.setText(initialTime)
                if (!finalTime.isValid()) {
                    setupFinalTimePicker()
                    finalTimePicker.show()
                }
            },
            hour,
            minutes,
            false
        )
    }

    private fun setupFinalTimePicker() {
        val hour: Int
        val minutes: Int

        val finalTimeText = finalTime.timeEditText.text.toString()
        val initTimeText = initTime.timeEditText.text.toString()
        val isFinalTimeComplete = finalTimeText.replace(DATE_TIME_PLACEHOLDER.toRegex(), "").length == 4

        if (isFinalTimeComplete) {
            val time = finalTimeText.split(":")
            hour = time[0].toInt()
            minutes = time[1].toInt()
        } else {
            val time = initTimeText.split(":")
            hour = time[0].toInt()
            minutes = time[1].toInt()
        }

        finalTimePicker = TimePickerDialog(
            context,
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                val mHours: String = if (hourOfDay < 10 )
                    "0$hourOfDay"
                else
                    hourOfDay.toString()

                val mMinutes: String = if (minute < 10)
                    "0$minute"
                else
                    minute.toString()

                val finalTime = "$mHours:$mMinutes"
                this.finalTime.timeEditText.setText(finalTime)
            },
            hour,
            minutes,
            false
        )


    }
}