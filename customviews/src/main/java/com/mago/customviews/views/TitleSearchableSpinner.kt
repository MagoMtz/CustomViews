package com.mago.customviews.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.mago.customviews.R

/**
 * @author by jmartinez
 * @since 04/02/2020.
 */
class TitleSearchableSpinner(context: Context, attributeSet: AttributeSet) :
    LinearLayout(context, attributeSet) {

    private lateinit var tvTitleHint: TextView
    var spinner: SearchableSpinner? = null
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }

    private var titleHintText: CharSequence = ""
        get() = tvTitleHint.text
        set(value) {
            field = value
            tvTitleHint.text = value
            invalidate()
            requestLayout()
        }

    private var isMandatory: Boolean = false
        set(value) {
            field = value
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
        View.inflate(context, R.layout.title_searchable_spinner, this)

        val sets = intArrayOf(R.attr.titleHint)
        val typedArray = context.obtainStyledAttributes(attributeSet, sets)
        val title = typedArray.getText(0)
        typedArray.recycle()


        context.theme.obtainStyledAttributes(attributeSet, R.styleable.TitleSearchableSpinner, 0, 0)
            .apply {
                try {
                    isMandatory = getBoolean(R.styleable.TitleSearchableSpinner_isMandatory, false)
                    hintText = getString(R.styleable.TitleSearchableSpinner_hintText)!!
                    spinnerHeight = getDimension(R.styleable.TitleSearchableSpinner_spinnerHeight, resources.getDimension(R.dimen.spinner_min_height))
                } finally {
                    recycle()
                }
            }

        initComponents()
        titleHintText = title
    }

    private fun initComponents() {
        tvTitleHint = findViewById(R.id.tv_title)
        spinner = findViewById(R.id.sp_searchable)

        spinner?.isMandatory = isMandatory
        spinner?.setHintText(hintText)
        spinner?.spinnerHeight = spinnerHeight
    }

}