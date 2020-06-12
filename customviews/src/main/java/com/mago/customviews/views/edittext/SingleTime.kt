package com.mago.customviews.views.edittext

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.text.Editable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.mago.customviews.R
import com.mago.customviews.util.Constants.TIME_PLACE_HOLDER

/**
 * @author by jmartinez
 * @since 11/06/2020.
 */
class SingleTime: LinearLayout {
    private lateinit var attributeSet: AttributeSet
    // Views
    private var tvTitle: TextView = TextView(context)
    private lateinit var lyTime: LinearLayout
    lateinit var btnTime: ImageView
    var timeEditText: TimeEditText = TimeEditText(context)
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }

    // Attributes
    var title: String = ""
        set(value) {
            field = value
            tvTitle.text = value
            invalidate()
            requestLayout()
        }
    var isMandatory: Boolean = false
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
        context.theme.obtainStyledAttributes(attributeSet, R.styleable.SingleTime, 0, 0)
            .apply {
                try {
                    isMandatory = getBoolean(R.styleable.SingleTime_isMandatory, false)
                    getString(R.styleable.SingleTime_title)?.let {
                        title = it
                    }
                } finally {
                    recycle()
                }
            }
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val isNotValid = timeEditText.text.toString().let {
            (it.isEmpty() || it == TIME_PLACE_HOLDER)
        }

        canvas?.apply {

            tvTitle.visibility = if (isValid())
                View.VISIBLE
            else
                View.INVISIBLE

            val cBounds = RectF(
                lyTime.left.toFloat(),
                lyTime.top.toFloat(),
                lyTime.right.toFloat(),
                lyTime.bottom.toFloat()
            )

            if (isMandatory)
                if (isNotValid)
                    drawRoundRect(cBounds, 10F, 10F, frameAlertPaint)
                else
                    drawRoundRect(cBounds, 10F, 10F, framePaint)
            else
                drawRoundRect(cBounds, 10F, 10F, framePaint)

        }

    }

    private fun init() {
        View.inflate(context, R.layout.single_time, this)

        initComponents()
        setClickListeners()
        setWillNotDraw(false)
    }

    private fun initComponents() {
        tvTitle = findViewById(R.id.tv_title)
        lyTime = findViewById(R.id.ly_time)
        timeEditText = findViewById(R.id.time_edit_text)
        btnTime = findViewById(R.id.btn_time)

        tvTitle.text = title
        timeEditText.hint = TIME_PLACE_HOLDER
    }

    private fun setClickListeners() {
        btnTime.setOnClickListener {
            setTimePicker()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setTimePicker() {
        val initialTimePicker = TimePickerDialog(
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

                timeEditText.setText("$mHours:$mMinutes")
            },
            12, 0, false
        )

        initialTimePicker.show()
    }

    fun setText(text: String?) {
        timeEditText.setText(text)
    }

    fun setText(resId: Int) {
        timeEditText.setText(resId)
    }

    fun getText(): Editable? = timeEditText.text

    fun enableViews(isEnabled: Boolean) {
        tvTitle.isEnabled = isEnabled
        lyTime.isEnabled = isEnabled
        timeEditText.isEnabled = isEnabled
        btnTime.isEnabled = isEnabled
    }

    fun isValid(): Boolean {
        val text = timeEditText.text.toString()
        return if (text.isNotEmpty())
            text != TIME_PLACE_HOLDER
        else
            text.isNotEmpty()
    }

}