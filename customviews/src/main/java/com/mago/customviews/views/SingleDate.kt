package com.mago.customviews.views

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.mago.customviews.R
import com.mago.customviews.util.Constants.DATE_PLACE_HOLDER
import com.mago.customviews.util.Regex.DATE
import java.util.*

/**
 * @author by jmartinez
 * @since 07/02/2020.
 */
class SingleDate(context: Context, attributeSet: AttributeSet) :
    LinearLayout(context, attributeSet) {

    // Views
    private var tvTitle: TextView? = null
    private lateinit var lyDate: LinearLayout
    lateinit var btnCalendar: ImageView
    var dateEditText: DateEditText? = null
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }

    // Attributes
    var titleHint: String = ""
        set(value) {
            field = value
            tvTitle?.text = value
            invalidate()
            requestLayout()
        }
    var isMandatory: Boolean = false
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

    // Paint objects
    private val framePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.border)
        style = Paint.Style.STROKE
        strokeWidth = 3F
    }

    private val frameAlertPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.frame_invalid)
        style = Paint.Style.STROKE
        strokeWidth = 3F
    }

    init {
        View.inflate(context, R.layout.single_date, this)

        context.theme.obtainStyledAttributes(attributeSet, R.styleable.SingleDate, 0, 0)
            .apply {
                try {
                    isMandatory = getBoolean(R.styleable.SingleDate_isMandatory, false)
                    getString(R.styleable.SingleDate_titleHint)?.let {
                        titleHint = it
                    }
                    futureDate = getBoolean(R.styleable.SingleDate_futureDate, false)
                } finally {
                    recycle()
                }
            }

        initComponents()
        setWillNotDraw(false)
        setClickListeners()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val textIsEmpty =
            dateEditText?.text.toString().let { (it.isEmpty() || it == DATE_PLACE_HOLDER) }

        //tvTitle.visibility = if (textIsEmpty) View.INVISIBLE else View.VISIBLE

        canvas?.apply {
            val cBounds = RectF(
                lyDate.left.toFloat(),
                lyDate.top.toFloat(),
                lyDate.right.toFloat(),
                lyDate.bottom.toFloat()
            )

            if (isMandatory)
                if (textIsEmpty)
                    drawRoundRect(cBounds, 10F, 10F, frameAlertPaint)
                else
                    drawRoundRect(cBounds, 10F, 10F, framePaint)
            else
                drawRoundRect(cBounds, 10F, 10F, framePaint)

        }


    }

    private fun initComponents() {
        tvTitle = findViewById(R.id.tv_title)
        lyDate = findViewById(R.id.ly_date)
        dateEditText = findViewById(R.id.date_edit_text)
        btnCalendar = findViewById(R.id.btn_calendar)

        tvTitle?.text = titleHint
        dateEditText!!.hint = DATE_PLACE_HOLDER
    }

    private fun setClickListeners() {
        btnCalendar.setOnClickListener {
            setDatePicker()
        }
    }

    private fun setDatePicker() {
        val calendar = Calendar.getInstance()
        var day = calendar.get(Calendar.DAY_OF_MONTH)
        var month = calendar.get(Calendar.MONTH)
        var year = calendar.get(Calendar.YEAR)

        val dateText = dateEditText!!.text.toString()
        val dateIsComplete = dateText.replace(DATE.toRegex(), "").length == 8

        if (dateText.isNotEmpty() && dateIsComplete) {
            val date = dateEditText!!.text.toString().split("/")
            day = date[0].toInt()
            month = date[1].toInt() - 1
            year = date[2].toInt()
        }
        val initialDatePicker = DatePickerDialog(
            context!!,
            DatePickerDialog.OnDateSetListener { _, mYear, monthOfYear, dayOfMonth ->
                val initialDate =
                    (if (dayOfMonth.toString().length < 2) "0".plus(dayOfMonth.toString()) else dayOfMonth.toString()) + "/" +
                            (if ((monthOfYear + 1).toString().length < 2) "0" + (monthOfYear + 1).toString() else (monthOfYear + 1).toString()) + "/" + mYear
                dateEditText!!.setText(initialDate)
            }, year, month, day
        )
        if (!futureDate)
            initialDatePicker.datePicker.maxDate = Date().time

        initialDatePicker.show()
    }

}