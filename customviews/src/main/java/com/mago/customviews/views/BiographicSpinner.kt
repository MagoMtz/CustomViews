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
class BiographicSpinner (context: Context, attributeSet: AttributeSet): LinearLayout(context, attributeSet) {

    private lateinit var tvTitle: TextView
    var spinner: CustomSpinner? = null
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


    init {
        View.inflate(context, R.layout.biographic_spinner, this)
        val sets = intArrayOf(R.attr.tittle)
        val typedArray = context.obtainStyledAttributes(attributeSet, sets)
        val title = typedArray.getText(0)
        typedArray.recycle()

        initComponents()

        tittleText = title
    }

    private fun initComponents() {
        tvTitle = findViewById(R.id.tv_tittle)
        spinner = findViewById(R.id.sp_custom)
    }

}