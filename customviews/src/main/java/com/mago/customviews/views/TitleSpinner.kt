package com.mago.customviews.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.mago.customviews.R

/**
 * @author by jmartinez
 * @since 17/01/2020.
 */
class TitleSpinner (context: Context, attributeSet: AttributeSet): LinearLayout(context, attributeSet) {

    private lateinit var tvTitle: TextView
    var spinner: CustomSpinner? = null
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
    private var hintText: String = ""
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }
    var spinnerHeight: Float = 0F
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }

    init {
        View.inflate(context, R.layout.title_spinner, this)
        val sets = intArrayOf(R.attr.titleHint)
        val typedArray = context.obtainStyledAttributes(attributeSet, sets)
        val title = typedArray.getText(0)
        typedArray.recycle()

        context.theme.obtainStyledAttributes(attributeSet, R.styleable.TitleSpinner, 0, 0)
            .apply {
                try {
                    hintText = getString(R.styleable.TitleSpinner_hintText)!!
                    spinnerHeight = getDimension(R.styleable.TitleSpinner_spinnerHeight, resources.getDimension(R.dimen.spinner_min_height))
                } finally {
                    recycle()
                }
            }

        initComponents()

        titleText = title
    }

    private fun initComponents() {
        tvTitle = findViewById(R.id.tv_title)
        spinner = findViewById(R.id.sp_custom)

        spinner?.titleHint = hintText
        spinner?.spinnerHeight = spinnerHeight
    }

}