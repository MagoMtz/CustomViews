package com.mago.customviews.views.title

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import com.mago.customviews.R
import com.mago.customviews.views.spinner.CustomSpinner

/**
 * @author by jmartinez
 * @since 17/01/2020.
 */
class TitleSpinner : LinearLayout {
    private lateinit var attributeSet: AttributeSet

    private var tvTitleText: TextView = TextView(context)
    var titleText: CharSequence = ""
        set(value) {
            tvTitleText.text = value
            field = value
            invalidate()
            requestLayout()
        }
    var spinner: CustomSpinner = CustomSpinner(context)
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }

    var isMandatory: Boolean = false
        set(value) {
            field = value
            spinner.isMandatory = isMandatory
            requestLayout()
            invalidate()
        }
    var paintArrow: Boolean = true
        set(value) {
            field = value
            spinner.isMandatory = paintArrow
            requestLayout()
            invalidate()
        }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        this.attributeSet = attributeSet
        setupAttributes()
        init()
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int)
            : super(
        context, attributeSet, defStyleAttr
    ) {
        this.attributeSet = attributeSet
        setupAttributes()
        init()
    }

    constructor(context: Context) : super(context) {
        init()
    }

    private fun init() {
        View.inflate(context, R.layout.title_spinner, this)

        initComponents()
        setWillNotDraw(false)
    }

    private fun setupAttributes() {
        context.theme.obtainStyledAttributes(attributeSet, R.styleable.TitleSpinner, 0, 0)
            .apply {
                try {
                    isMandatory = getBoolean(R.styleable.TitleSpinner_isMandatory, false)
                    paintArrow = getBoolean(R.styleable.TitleSpinner_paintArrow, true)
                    getString(R.styleable.TitleSearchableSpinner_title)?.let {
                        titleText = it
                    }
                } finally {
                    recycle()
                }
            }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            spinner.let {
                tvTitleText.visibility = if (it.selectedItemPosition == 0)
                    View.INVISIBLE
                else
                    View.VISIBLE
            }
        }
    }

    private fun initComponents() {
        tvTitleText = findViewById(R.id.tv_title)
        spinner = findViewById(R.id.sp_custom)

        spinner.paintArrow = paintArrow
        tvTitleText.text = titleText
    }

    fun isValid(): Boolean = spinner.isValid

    fun enableViews(isEnabled: Boolean) {
        tvTitleText.isEnabled = isEnabled
        spinner.isEnabled = isEnabled
    }

    fun setAdapter(adapter: ArrayAdapter<*>) {
        spinner.adapter = adapter
    }

    fun getAdapter() = spinner.adapter

    fun setSelection(pos: Int) {
        spinner.setSelection(pos)
    }

    fun getSelection() = spinner.selectedItem


}