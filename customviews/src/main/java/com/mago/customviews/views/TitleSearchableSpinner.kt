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
class TitleSearchableSpinner(context: Context, attributeSet: AttributeSet): LinearLayout(context, attributeSet) {

    private lateinit var tvTitle: TextView
    var spinner: SearchableSpinner? = null
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }

    private var tittleText: CharSequence = ""
        get() = tvTitle.text
        set(value) {
            field = value
            tvTitle.text = value
            invalidate()
            requestLayout()
        }

    private var isMandatory: Boolean = false
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }

    init {
        View.inflate(context, R.layout.title_searchable_spinner, this)

        val sets = intArrayOf(R.attr.tittle)
        val typedArray = context.obtainStyledAttributes(attributeSet, sets)
        val title = typedArray.getText(0)
        typedArray.recycle()


        context.theme.obtainStyledAttributes(attributeSet, R.styleable.TitleSearchableSpinner, 0, 0)
            .apply {
                try {
                    isMandatory = getBoolean(R.styleable.TitleSearchableSpinner_isMandatory, false)
                } finally {
                    recycle()
                }
            }


        initComponents()
        tittleText = title
    }

    private fun initComponents() {
        tvTitle = findViewById(R.id.tv_tittle)
        spinner = findViewById(R.id.sp_searchable)
        spinner?.isMandatory = isMandatory
    }

}