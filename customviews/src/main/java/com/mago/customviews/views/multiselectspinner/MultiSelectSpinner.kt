package com.mago.customviews.views.multiselectspinner

import android.content.Context
import android.util.AttributeSet
import com.mago.customviews.R
import com.mago.customviews.views.SearchableSpinner

/**
 * @author by jmartinez
 * @since 18/03/2020.
 */
class MultiSelectSpinner(context: Context, attributeSet: AttributeSet): SearchableSpinner(context, attributeSet, true) {

    init {
        context.theme.obtainStyledAttributes(attributeSet, R.styleable.MultiSelectSpinner, 0, 0)
            .apply {
                try {
                    isMandatory = getBoolean(R.styleable.MultiSelectSpinner_isMandatory, false)
                    spinnerHeight = getDimension(
                        R.styleable.MultiSelectSpinner_spinnerHeight,
                        resources.getDimension(R.dimen.spinner_min_height)
                    )

                    background = null
                } finally {
                    recycle()
                }
            }
    }

}