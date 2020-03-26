package com.mago.customviews.views.title

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.mago.customviews.R
import com.mago.customviews.views.spinner.SearchableSpinner

/**
 * @author by jmartinez
 * @since 04/02/2020.
 */
class TitleSearchableSpinner : LinearLayout {
    private lateinit var attributeSet: AttributeSet
    // Views
    private var tvTitleText: TextView = TextView(context)
    var titleText: CharSequence = ""
        //get() = tvTitleText.text
        set(value) {
            field = value
            tvTitleText.text = value
            requestLayout()
            invalidate()
        }
    var spinner: SearchableSpinner = SearchableSpinner(context)
        set(value) {
            field = value
            requestLayout()
            invalidate()
        }
    // Attr
    private var isMandatory: Boolean = false
        set(value) {
            field = value
            requestLayout()
            invalidate()
        }
    /*
    var spinnerHeight: Float = 0F
        set(value) {
            field = value
            requestLayout()
            invalidate()
        }

     */
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        this.attributeSet = attributeSet
        setupAttributes()
        init()
    }
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int)
            : super(context, attributeSet, defStyleAttr) {
        this.attributeSet = attributeSet
        setupAttributes()
        init()
    }
    constructor(context: Context) : super(context) {
        init()
    }

    private fun init() {
        View.inflate(context, R.layout.title_searchable_spinner, this)

        initComponents()
        setWillNotDraw(false)
    }

    private fun setupAttributes() {
        context.theme.obtainStyledAttributes(attributeSet, R.styleable.TitleSearchableSpinner, 0, 0)
            .apply {
                try {
                    isMandatory = getBoolean(R.styleable.TitleSearchableSpinner_isMandatory, false)
                    getString(R.styleable.TitleSearchableSpinner_titleHint)?.let {
                        titleText = it
                    }
                    /*spinnerHeight = getDimension(
                        R.styleable.TitleSearchableSpinner_spinnerHeight,
                        resources.getDimension(R.dimen.spinner_min_height)
                    )

                     */
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
        spinner = findViewById(R.id.sp_searchable)

        spinner.isMandatory = isMandatory
        tvTitleText.text = titleText
        //spinner.spinnerHeight = spinnerHeight
    }

}