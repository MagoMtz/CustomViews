package com.mago.customviews.views.title

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.mago.customviews.R
import com.mago.customviews.views.spinner.CustomSpinner

/**
 * @author by jmartinez
 * @since 17/01/2020.
 */
class TitleSpinner: LinearLayout {
    private lateinit var attributeSet: AttributeSet

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        this.attributeSet = attributeSet
        init()
    }
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    ) {
        this.attributeSet = attributeSet
        init()
    }
    constructor(context: Context) : super(context)

    private lateinit var tvTitle: TextView
    var spinner: CustomSpinner = CustomSpinner(context)
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }

    private var titleText: CharSequence = ""
        get() = tvTitle.text
        set(value) {
            field = value
            tvTitle.text = value
            invalidate()
            requestLayout()
        }
    /*
    var spinnerHeight: Float = 0F
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }


     */
    var isMandatory: Boolean = false
        set(value) {
            field = value
            requestLayout()
            invalidate()
        }

    private fun init() {
        View.inflate(context, R.layout.title_spinner, this)
        val sets = intArrayOf(R.attr.titleHint)
        val typedArray = context.obtainStyledAttributes(attributeSet, sets)
        val title = typedArray.getText(0)
        typedArray.recycle()

        context.theme.obtainStyledAttributes(attributeSet, R.styleable.TitleSpinner, 0, 0)
            .apply {
                try {
                    /*
                    spinnerHeight = getDimension(
                        R.styleable.TitleSpinner_spinnerHeight,
                        resources.getDimension(R.dimen.spinner_min_height)
                    )

                     */
                    isMandatory = getBoolean(R.styleable.TitleSpinner_isMandatory, false)
                } finally {
                    recycle()
                }
            }

        initComponents()
        titleText = title
        setWillNotDraw(false)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            spinner.let {
                tvTitle.visibility = if (it.selectedItemPosition == 0)
                    View.INVISIBLE
                else
                    View.VISIBLE
            }
        }
    }

    private fun initComponents() {
        tvTitle = findViewById(R.id.tv_title)
        spinner = findViewById(R.id.sp_custom)

        //spinner.spinnerHeight = spinnerHeight
        spinner.isMandatory = isMandatory
    }

}