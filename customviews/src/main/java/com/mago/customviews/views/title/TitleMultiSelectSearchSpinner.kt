package com.mago.customviews.views.title

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.mago.customviews.R
import com.mago.customviews.util.CommonUtils
import com.mago.customviews.views.spinner.multiselectspinner.MultiSelectSearchSpinner

class TitleMultiSelectSearchSpinner : LinearLayout {
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

    private lateinit var tvTitleHint: TextView
    var spinner: MultiSelectSearchSpinner = MultiSelectSearchSpinner(context)
        set(value) {
            field = value
            requestLayout()
            invalidate()
        }

    private var titleHintText: CharSequence = ""
        get() = tvTitleHint.text
        set(value) {
            field = value
            tvTitleHint.text = value
            requestLayout()
            invalidate()
        }
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

    private fun init() {
        View.inflate(context, R.layout.title_multi_select_search_spinner, this)

        val sets = intArrayOf(R.attr.titleHint)
        val typedArray = context.obtainStyledAttributes(attributeSet, sets)
        val title = typedArray.getText(0)
        typedArray.recycle()


        context.theme.obtainStyledAttributes(
            attributeSet,
            R.styleable.TitleMultiSelectSearchSpinner,
            0,
            0
        )
            .apply {
                try {
                    isMandatory =
                        getBoolean(R.styleable.TitleMultiSelectSearchSpinner_isMandatory, false)
                    /*
                    spinnerHeight = getDimension(
                        R.styleable.TitleMultiSelectSearchSpinner_spinnerHeight,
                        resources.getDimension(R.dimen.spinner_min_height)
                    )

                     */
                } finally {
                    recycle()
                }
            }

        initComponents()
        titleHintText = title
        setWillNotDraw(false)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            spinner.let {
                tvTitleHint.visibility = if (it.getSelectedItems().isEmpty())
                    View.INVISIBLE
                else
                    View.VISIBLE
            }
        }
    }

    private fun initComponents() {
        tvTitleHint = findViewById(R.id.tv_title)
        spinner = findViewById(R.id.sp_searchable)

        spinner.isMandatory = isMandatory
        //spinner.spinnerHeight = spinnerHeight - tvTitleHint.height
    }

}